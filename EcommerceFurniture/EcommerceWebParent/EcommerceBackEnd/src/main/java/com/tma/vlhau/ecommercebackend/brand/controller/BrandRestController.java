package com.tma.vlhau.ecommercebackend.brand.controller;

import com.tma.vlhau.ecommercebackend.brand.DTO.CategoryDTO;
import com.tma.vlhau.ecommercebackend.brand.exception.BrandNotFoundException;
import com.tma.vlhau.ecommercebackend.brand.exception.BrandRestNotFoundException;
import com.tma.vlhau.ecommercebackend.brand.service.BrandService;
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
public class BrandRestController {

    @Autowired
    BrandService brandService;

    @PostMapping("/brands/check_brand")
    public String checkDuplicateBrand(Integer id, String name) {
        return brandService.checkBrandUnique(id, name);
    }

    @GetMapping("/brands/{id}/categories")
    public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name = "id") Integer brandId) throws BrandRestNotFoundException {
        List<CategoryDTO> listCategoryDTO = new ArrayList<>();
        try {
            Brand brand = brandService.get(brandId);
            Set<Category> categories = brand.getCategories();
            categories.forEach(category -> {
                CategoryDTO categoryDTO = new CategoryDTO(category.getId(), category.getName());
                listCategoryDTO.add(categoryDTO);
            });
        } catch (BrandNotFoundException ex) {
            throw new BrandRestNotFoundException();
        }
        return listCategoryDTO;
    }

}
