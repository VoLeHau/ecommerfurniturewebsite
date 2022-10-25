package com.tma.vlhau.ecommercebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.tma.vlhau.ecommercecommon.entity", "com.tma.vlhau.ecommercebackend.user"})
public class EcommerceBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceBackEndApplication.class, args);
    }

}
