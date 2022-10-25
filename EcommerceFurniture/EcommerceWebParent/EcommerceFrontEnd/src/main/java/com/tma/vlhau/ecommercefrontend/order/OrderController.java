package com.tma.vlhau.ecommercefrontend.order;

import com.tma.vlhau.ecommercecommon.entity.Customer;
import com.tma.vlhau.ecommercecommon.entity.order.Order;
import com.tma.vlhau.ecommercecommon.entity.order.OrderStatus;
import com.tma.vlhau.ecommercecommon.entity.order.OrderTrack;
import com.tma.vlhau.ecommercefrontend.ControllerHelper;
import com.tma.vlhau.ecommercefrontend.Utility;
import com.tma.vlhau.ecommercefrontend.customer.CustomerService;
import com.tma.vlhau.ecommercefrontend.shoppingcart.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrderController {

    @Autowired private OrderService orderService;
    @Autowired private CustomerService customerService;
    @Autowired private ControllerHelper controllerHelper;

    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private OrderTrackRepository orderTrackRepository;
    @GetMapping("/orders")
    public String listFirstPage(Model model, HttpServletRequest request) {
        return listOrderByPage(model, request, 1, "orderTime", "desc", null);
    }

    @GetMapping("/orders/page/{pageNum}")
    private String listOrderByPage(Model model, HttpServletRequest request,
                                   @PathVariable(name = "pageNum") int pageNum,
                                   String sortField, String sortDir, String orderKeyword) {
        Customer customer = controllerHelper.getAuthenticatedCustomer(request);

        Page<Order> orderPage = orderService.listForCustomerByPage(customer, pageNum, sortField, sortDir, orderKeyword);
        List<Order> listOrders = orderPage.getContent();
        int amountProduct = cartItemRepository.getamountProductByCustomer(customer);

        model.addAttribute("amountProduct",amountProduct);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalItems", orderPage.getTotalElements());
        model.addAttribute("currenPage", pageNum);
        model.addAttribute("listOrders", listOrders);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortOrder", sortDir);
        model.addAttribute("orderKeyword", orderKeyword);
        model.addAttribute("moduleURL", "/orders");
        model.addAttribute("reverseSortOrder", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("currentPage", pageNum);

        long startCount = (pageNum - 1) * OrderService.ORDERS_PER_PAGE + 1;
        model.addAttribute("startCount", startCount);

        long endCount = startCount + OrderService.ORDERS_PER_PAGE - 1;
        if (endCount > orderPage.getTotalElements()) {
            endCount = orderPage.getTotalElements();
        }

        int idOrderPaidCancel = 0;
        for(Order order : orderService.getAll()){

            int statusPaid = 0;
            int statusCancel = 0;
            if(order.getOrderTracks().get(0).getStatus().equals(OrderStatus.PAID)){
                statusPaid = 1;
            }
            if(order.getOrderTracks().get(order.getOrderTracks().size()-1).getStatus().equals(OrderStatus.CANCELLED)){
                statusCancel = 1;
            }

            if((statusPaid==1 && statusCancel ==1)){
                idOrderPaidCancel = order.getId();
            }

        }

        model.addAttribute("idOrderPaidCancel",idOrderPaidCancel);
        model.addAttribute("endCount", endCount);
        model.addAttribute("statusRefunded",OrderStatus.REFUNDED);
        model.addAttribute("statusCancel",OrderStatus.CANCELLED);
        model.addAttribute("statusReturnRequested",OrderStatus.RETURN_REQUESTED);
        model.addAttribute("statusReturned",OrderStatus.RETURNED);
        return "orders/orders_customer";
    }

    public Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        return customerService.getCustomerByEmail(email);
    }

    @GetMapping("/orders/detail/{id}")
    public String viewOrderDetails(Model model, @PathVariable(name = "id") Integer id, HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);

        Order order = orderService.getOrder(id, customer);
        model.addAttribute("order", order);

        int amountProduct = cartItemRepository.getamountProductByCustomer(customer);

        model.addAttribute("amountProduct",amountProduct);
        List<OrderTrack> listOrderTrack = orderTrackRepository.getOrderTrackByOrder(order);
        OrderTrack orderTrack = listOrderTrack.get(listOrderTrack.size()-1);
        model.addAttribute("orderStatus",orderTrack.getStatus());

        return "orders/order_details_modal";
    }

}
