package com.tma.vlhau.ecommercebackend.product.controller;

import com.tma.vlhau.ecommercebackend.product.DTO.ProductDTO;
import com.tma.vlhau.ecommercebackend.product.service.ProductService;
import com.tma.vlhau.ecommercecommon.entity.product.Product;
import com.tma.vlhau.ecommercecommon.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {

    @Autowired
	ProductService productService;

    @PostMapping("/products/check_product")
    public String checkDuplicateProduct(Integer id, String name, String alias) {
        return productService.checkProductUnique(id, name, alias);
    }
    
	@GetMapping("/products/get/{id}")
	public ProductDTO getProductInfo(@PathVariable("id") Integer id)
			throws ProductNotFoundException {
		Product product = productService.get(id);
		return new ProductDTO(product.getName(), product.getMainImagePath(), 
				product.getDiscountPrice(), product.getCost());
	}

}
