package com.tma.vlhau.ecommercefrontend;

import com.tma.vlhau.ecommercecommon.entity.Category;
import com.tma.vlhau.ecommercecommon.entity.Customer;
import com.tma.vlhau.ecommercefrontend.category.CategoryService;
import com.tma.vlhau.ecommercefrontend.customer.CustomerService;
import com.tma.vlhau.ecommercefrontend.shoppingcart.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class MainController {

	@Autowired
	private CategoryService categoryService;
	@Autowired private CustomerService customerService;

	@Autowired private CartItemRepository cartItemRepository;
	@Autowired private ControllerHelper controllerHelper;
    @GetMapping("")
    public String viewHomePage(Model model, HttpServletRequest request){
		List<Category> listCategories = categoryService.listNoChildrenCategories();

		Customer customer = controllerHelper.getAuthenticatedCustomer(request);
		int amountProduct = cartItemRepository.getamountProductByCustomer(customer);


		model.addAttribute("amountProduct",amountProduct);
		model.addAttribute("listCategories", listCategories);
		return "index";
    }
	
	@GetMapping("/login")
	public String viewLoginPage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}
		return "redirect:/";
	}


}
