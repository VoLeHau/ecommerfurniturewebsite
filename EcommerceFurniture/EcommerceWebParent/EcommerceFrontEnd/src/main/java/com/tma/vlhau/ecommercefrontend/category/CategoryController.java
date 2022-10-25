package com.tma.vlhau.ecommercefrontend.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

//    @GetMapping("")
//    public String viewHomePage(Model model) {
//        List<Category> listCategories = categoryService.listNoChildrenCategories();
//
//        model.addAttribute("listCategories", listCategories);
//        return "index";
//    }
}
