package com.tma.vlhau.ecommercefrontend.comment.controller;

import com.tma.vlhau.ecommercecommon.entity.Comment;
import com.tma.vlhau.ecommercecommon.entity.CommentImage;
import com.tma.vlhau.ecommercecommon.entity.Customer;
import com.tma.vlhau.ecommercecommon.entity.product.Product;
import com.tma.vlhau.ecommercecommon.exception.ProductNotFoundException;
import com.tma.vlhau.ecommercefrontend.ControllerHelper;
import com.tma.vlhau.ecommercefrontend.FileUploadUtil;
import com.tma.vlhau.ecommercefrontend.comment.service.CommentService;
import com.tma.vlhau.ecommercefrontend.commentimage.repository.CommentImageRepository;
import com.tma.vlhau.ecommercefrontend.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentImageRepository commentImageRepository;

    @Autowired
    private ProductService productService;
    @Autowired
    private ControllerHelper controllerHelper;

    @PostMapping("/p/{product_alias}/addComment")
    public String addComment(@RequestParam("file[]") MultipartFile[] multipartFile, String text, Integer rating, @PathVariable("product_alias") String alias,HttpServletRequest request) throws ProductNotFoundException, IOException {

        Comment comment = new Comment();
        Customer customer = controllerHelper.getAuthenticatedCustomer(request);
        LocalDateTime localDateTime = LocalDateTime.now();
        comment.setDate(localDateTime);
        comment.setRating(rating);
        comment.setText(text);
        comment.setCustomer(customer);
        Product product = productService.getProductByAlias(alias);
        comment.setProduct(product);

        commentService.save(comment);
        for(MultipartFile multipart : multipartFile){
            CommentImage commentImage = new CommentImage();

            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipart.getOriginalFilename()));
            commentImage.setImage(fileName);
            commentImage.setComment(comment);
            commentImageRepository.save(commentImage);
            String uploadDir = "../" + FileUploadUtil.COMMENT_IMAGE_DIR_NAME + commentImage.getId() + "/";
            FileUploadUtil.cleanDirectory(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipart);
        }
        return "redirect:/p/" + alias;

    }
}
