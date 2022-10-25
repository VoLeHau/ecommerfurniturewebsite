package com.tma.vlhau.ecommercefrontend.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerRestController {

    @Autowired
    CustomerService customerService;

    @PostMapping("/customers/check_email_unique")
    public String checkDuplicateEmail(@Param("email") String email) {
        return customerService.isEmailUnique(email) ? "OK" : "Duplicated";
    }

    @PostMapping("/customers/check_phone_number_unique")
    public String checkDuplicatePhoneNumber(@Param("phoneNumber") String phoneNumber) {
        return customerService.isPhoneNumberUnique(phoneNumber) ? "OK" : "Duplicated";
    }

}
