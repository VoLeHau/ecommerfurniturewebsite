package com.tma.vlhau.ecommercebackend.category.controller;

import com.tma.vlhau.ecommercebackend.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {

    @Autowired
    CategoryService categoryService;

    @PostMapping("/categories/check_name_alias")
    public String checkDuplicateEmail(Integer id, String name, String alias) {
        return categoryService.isCategoryUnique(id, name, alias);
    }

}
