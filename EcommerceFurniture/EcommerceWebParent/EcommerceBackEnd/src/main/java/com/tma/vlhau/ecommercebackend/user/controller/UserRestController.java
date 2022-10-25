package com.tma.vlhau.ecommercebackend.user.controller;

import com.tma.vlhau.ecommercebackend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/users/check_email")
    public String checkDuplicateEmail(Integer id, String email){
        return userService.isEmailUnique(id, email) ? "OK" : "Duplicated";
    }

}
