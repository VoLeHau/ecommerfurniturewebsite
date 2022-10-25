package com.tma.vlhau.ecommercebackend.role.controller;

import com.tma.vlhau.ecommercebackend.brand.DTO.CategoryDTO;
import com.tma.vlhau.ecommercebackend.brand.exception.BrandNotFoundException;
import com.tma.vlhau.ecommercebackend.brand.exception.BrandRestNotFoundException;
import com.tma.vlhau.ecommercebackend.role.service.RoleService;
import com.tma.vlhau.ecommercecommon.entity.Brand;
import com.tma.vlhau.ecommercecommon.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@RestController
public class RoleRestController {

    @Autowired
    RoleService roleService;

    @PostMapping("/roles/check_role")
    public String checkDuplicateRole(Integer id, String name) {
        return roleService.checkRoleUnique(id, name);
    }


}
