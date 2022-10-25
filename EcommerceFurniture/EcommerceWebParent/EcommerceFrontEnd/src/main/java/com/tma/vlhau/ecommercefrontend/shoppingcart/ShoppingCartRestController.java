package com.tma.vlhau.ecommercefrontend.shoppingcart;

import com.tma.vlhau.ecommercecommon.entity.CartItem;
import com.tma.vlhau.ecommercecommon.entity.Customer;
import com.tma.vlhau.ecommercecommon.entity.product.Product;
import com.tma.vlhau.ecommercecommon.exception.CustomerNotFoundException;
import com.tma.vlhau.ecommercefrontend.Utility;
import com.tma.vlhau.ecommercefrontend.customer.CustomerService;
import com.tma.vlhau.ecommercefrontend.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ShoppingCartRestController {
	
	@Autowired private ShoppingCartService shoppingCartService;
	@Autowired private CustomerService customerService;
	@Autowired private ProductService productService;
	@Autowired private CartItemRepository cartItemRepository;
	@PostMapping("/cart/add/{productId}/{quantity}")
	public String addProductToCart(@PathVariable("productId") Integer productId,
								   @PathVariable("quantity") Integer quantity, HttpServletRequest request, Model model) {
		try {			
			Customer customer  = getAuthenticatedCustomer(request);
			Integer updatedQuantity = shoppingCartService.addProduct(productId, quantity, customer);
			int amountProduct = cartItemRepository.getamountProductByCustomer(customer);

			model.addAttribute("amountProduct",amountProduct);
			return String.valueOf(updatedQuantity);
		}
		catch (CustomerNotFoundException ex){
			return "You must login to add this product to cart.";
		}
		catch(ShoppingCartException ex) {
			return ex.getMessage();
		}
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		if(email == null) {
			throw new CustomerNotFoundException("No authenticated customer");
		}
		
		return customerService.getCustomerByEmail(email);
	}
	
	@PostMapping("/cart/update/{productId}/{quantity}")
	public String updateQuantity(@PathVariable("productId") Integer productId,
			@PathVariable("quantity") Integer quantity, HttpServletRequest request, Model model) {
		
		try {			
			Customer customer  = getAuthenticatedCustomer(request);
			Product product = productService.findById(productId);
			CartItem cartItem = shoppingCartService.findByProductIdAndCustomer(product, customer);
			int numberProductTakeInStock = quantity - cartItem.getQuantity();

			float subtotal = shoppingCartService.updateQuantity(productId, quantity, customer);
			product.setQuantityInStock(product.getQuantityInStock() - numberProductTakeInStock);
			productService.save(product);
			int amountProduct = cartItemRepository.getamountProductByCustomer(customer);

			model.addAttribute("amountProduct",amountProduct);
			return String.valueOf(subtotal);
		}
		catch (CustomerNotFoundException ex){
			return "You must login to change quantity of cart.";
		}
				
	 }
	
	@DeleteMapping("/cart/remove/{productId}")
	public String removeProduct(@PathVariable("productId") Integer productId, HttpServletRequest request, Model model) {
		
		try {
			Customer customer  = getAuthenticatedCustomer(request);
            Product product = productService.findById(productId);
            CartItem cartItem = shoppingCartService.findByProductIdAndCustomer(product, customer);


            product.setQuantityInStock(product.getQuantityInStock() + cartItem.getQuantity());
            productService.save(product);
            shoppingCartService.removeProduct(productId, customer);
			int amountProduct = cartItemRepository.getamountProductByCustomer(customer);

			model.addAttribute("amountProduct",amountProduct);
			return "The product has been removed from your shopping cart.";
		}
		catch (CustomerNotFoundException e) {
			return "You must login to remove product.";
		}
		
	}
	
	
}
