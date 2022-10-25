package com.tma.vlhau.ecommercefrontend.navbar;

import com.tma.vlhau.ecommercecommon.entity.Customer;
import com.tma.vlhau.ecommercefrontend.ControllerHelper;
import com.tma.vlhau.ecommercefrontend.Utility;
import com.tma.vlhau.ecommercefrontend.customer.CustomerService;
import com.tma.vlhau.ecommercefrontend.order.OrderRepository;
import com.tma.vlhau.ecommercefrontend.shoppingcart.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NavbarController {


    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private ControllerHelper controllerHelper;

    @GetMapping("AboutUs")
    public String viewAboutUsPage(Model model, HttpServletRequest request) {
        Customer customer = controllerHelper.getAuthenticatedCustomer(request);
        int amountProduct = cartItemRepository.getamountProductByCustomer(customer);


        model.addAttribute("amountProduct",amountProduct);
        return "aboutus";
    }

    @GetMapping("Payments")
    public String viewPaymentPage(Model model,HttpServletRequest request) {
        Customer customer = controllerHelper.getAuthenticatedCustomer(request);
        int amountProduct = cartItemRepository.getamountProductByCustomer(customer);

        model.addAttribute("amountProduct",amountProduct);
        return "payment";
    }
    
}
