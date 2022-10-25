package com.tma.vlhau.ecommercefurniture;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.tma.vlhau.ecommercecommon.entity", "com.tma.vlhau.ecommercebackend.user"})
public class EcommerceFurnitureApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceFurnitureApplication.class, args);
    }

}
