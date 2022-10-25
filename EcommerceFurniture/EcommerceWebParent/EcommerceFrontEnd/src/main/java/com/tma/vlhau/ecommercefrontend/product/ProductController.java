package com.tma.vlhau.ecommercefrontend.product;

import com.tma.vlhau.ecommercecommon.entity.Category;
import com.tma.vlhau.ecommercecommon.entity.Comment;
import com.tma.vlhau.ecommercecommon.entity.Customer;
import com.tma.vlhau.ecommercecommon.entity.order.Order;
import com.tma.vlhau.ecommercecommon.entity.order.OrderDetail;
import com.tma.vlhau.ecommercecommon.entity.order.OrderTrack;
import com.tma.vlhau.ecommercecommon.entity.product.Product;
import com.tma.vlhau.ecommercecommon.exception.CategoryNotFoundException;
import com.tma.vlhau.ecommercecommon.exception.ProductNotFoundException;
import com.tma.vlhau.ecommercefrontend.ControllerHelper;
import com.tma.vlhau.ecommercefrontend.category.CategoryService;
import com.tma.vlhau.ecommercefrontend.comment.service.CommentService;
import com.tma.vlhau.ecommercefrontend.commentimage.repository.CommentImageRepository;
import com.tma.vlhau.ecommercefrontend.commentreply.service.CommentReplyService;
import com.tma.vlhau.ecommercefrontend.order.OrderDetailRepository;
import com.tma.vlhau.ecommercefrontend.order.OrderService;
import com.tma.vlhau.ecommercefrontend.order.OrderDetailService;
import com.tma.vlhau.ecommercefrontend.shoppingcart.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;
    @Autowired private ControllerHelper controllerHelper;
    @Autowired private CartItemRepository cartItemRepository;

    @Autowired private CommentService commentService;

    @Autowired private CommentReplyService commentReplyService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommentImageRepository commentImageRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @GetMapping("/c/{category_alias}")
    public String viewCategoryFirstPage(@PathVariable("category_alias") String alias,
                                        Model model, HttpServletRequest request) throws CategoryNotFoundException {
        return viewCategoryByPage(alias, 1, model,request);
    }

    @GetMapping("/c/{category_alias}/page/{pageNum}")
    public String viewCategoryByPage(@PathVariable("category_alias") String alias,
                               @PathVariable("pageNum") int pageNum,
                               Model model,HttpServletRequest request) throws CategoryNotFoundException {
        try {
            Customer customer = controllerHelper.getAuthenticatedCustomer(request);
            Category category = categoryService.getCategoryByAlias(alias);
            List<Category> listCategoryParents = categoryService.getCategoryParents(category);

            Page<Product> listProductsByCategory = productService.listByCategory(category.getId(), pageNum);
            List<Product> listProducts = listProductsByCategory.getContent();

            
            long startCount = (long) (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
            long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;

            if (endCount > listProductsByCategory.getTotalElements()) {
                endCount = listProductsByCategory.getTotalElements();
            }
            int amountProduct = cartItemRepository.getamountProductByCustomer(customer);

            model.addAttribute("amountProduct",amountProduct);
            model.addAttribute("startCount", startCount);
            model.addAttribute("endCount", endCount);
            model.addAttribute("currentPage", pageNum);
            model.addAttribute("totalItems", listProductsByCategory.getTotalElements());
            model.addAttribute("totalPages", listProductsByCategory.getTotalPages());
            model.addAttribute("listProducts", listProducts);
            model.addAttribute("listCategoryParents", listCategoryParents);
            model.addAttribute("pageTitle", category.getName());
            model.addAttribute("category", category);
            return "product/product_by_category";
        } catch (CategoryNotFoundException e) {
            return "error/404";
        }
    }

    @GetMapping("/p/{product_alias}")
    public String viewProductDetail(@PathVariable("product_alias") String alias, Model model,HttpServletRequest request) throws ProductNotFoundException{
        return viewCommentByPage(alias, 1, model,request);
    }

    @GetMapping("/p/{product_alias}/page/{pageNum}")
    public String viewCommentByPage(@PathVariable("product_alias") String alias,
                                     @PathVariable("pageNum") int pageNum,
                                     Model model,HttpServletRequest request) throws ProductNotFoundException{
        try {

            Customer customer = controllerHelper.getAuthenticatedCustomer(request);
            Product product = productService.getProductByAlias(alias);

            boolean isCustomer = false;
            int amountProduct =0;
            if (customer != null) {
                amountProduct = cartItemRepository.getamountProductByCustomer(customer);
                model.addAttribute("amountProduct", amountProduct);
                isCustomer = true;
            }

            Page<Comment> listCommentsPage = commentService.listByPage(pageNum, product);
            List<Comment> listComments = listCommentsPage.getContent();

            long startCount = (long) (pageNum - 1) * CommentService.COMMENT_PER_PAGE + 1;
            long endCount = startCount + CommentService.COMMENT_PER_PAGE - 1;

            if (endCount > listCommentsPage.getTotalElements()) {
                endCount = listCommentsPage.getTotalElements();
            }


            List<OrderDetail> listDetail = orderDetailRepository.getOrderDetailByOrder(customer);

            model.addAttribute("listDetail", listDetail);
            model.addAttribute("pageTitle", product.getShortName());
            model.addAttribute("isCustomer", isCustomer);
            model.addAttribute("product", product);
            model.addAttribute("listCommentsReply", commentReplyService.findAll());
            model.addAttribute("amountProduct",amountProduct);
            model.addAttribute("startCount", startCount);
            model.addAttribute("endCount", endCount);
            model.addAttribute("currentPage", pageNum);
            model.addAttribute("totalItems", listCommentsPage.getTotalElements());
            model.addAttribute("totalPages", listCommentsPage.getTotalPages());
            model.addAttribute("listComments", listComments);
            model.addAttribute("listCommentImages", commentImageRepository.findAll());

            return "product/product_detail";
        } catch (ProductNotFoundException e) {
            return "error/404";
        }
    }

    @GetMapping("/search")
    public String searchFirstPage(@Param("keyword") String keyword, Model model,HttpServletRequest request) {
        return searchByPage(keyword, model,1,request);
    }

    @GetMapping("/search/page/{pageNum}")
    public String searchByPage(@Param("keyword") String keyword, Model model,
                         @PathVariable("pageNum") int pageNum,HttpServletRequest request) {

        Customer customer = controllerHelper.getAuthenticatedCustomer(request);
        Page<Product> searchProductResults = productService.search(keyword, pageNum);
        List<Product> listProductResults = searchProductResults.getContent();

        long startCount = (long) (pageNum - 1) * ProductService.SEARCH_RESULTS_PER_PAGE + 1;
        long endCount = startCount + ProductService.SEARCH_RESULTS_PER_PAGE - 1;

        if (endCount > searchProductResults.getTotalElements()) {
            endCount = searchProductResults.getTotalElements();
        }
        int amountProduct = cartItemRepository.getamountProductByCustomer(customer);

        model.addAttribute("amountProduct",amountProduct);
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalItems", searchProductResults.getTotalElements());
        model.addAttribute("totalPages", searchProductResults.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("listProductResults", listProductResults);
        model.addAttribute("pageTitle",keyword + " - Search Results");

        return "product/search_result";
    }

}
