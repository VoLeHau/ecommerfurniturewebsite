package com.tma.vlhau.ecommercebackend.customer.controller;

import com.tma.vlhau.ecommercebackend.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {

    @Autowired
    CustomerService customerService;

    @PostMapping("/customers/check_email")
    public String checkDuplicateEmail(Integer id, String email) {
        if (customerService.isEmailUnique(id, email)) {
            return "OK";
        } else {
            return "Duplicated";
        }
    }

}
