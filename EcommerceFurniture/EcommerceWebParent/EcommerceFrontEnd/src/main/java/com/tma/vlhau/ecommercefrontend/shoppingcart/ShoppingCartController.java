package com.tma.vlhau.ecommercefrontend.shoppingcart;

import com.tma.vlhau.ecommercecommon.entity.Address;
import com.tma.vlhau.ecommercecommon.entity.CartItem;
import com.tma.vlhau.ecommercecommon.entity.Customer;
import com.tma.vlhau.ecommercecommon.entity.ShippingRate;
import com.tma.vlhau.ecommercecommon.exception.CustomerNotFoundException;
import com.tma.vlhau.ecommercefrontend.Utility;
import com.tma.vlhau.ecommercefrontend.address.AddressService;
import com.tma.vlhau.ecommercefrontend.customer.CustomerService;
import com.tma.vlhau.ecommercefrontend.order.OrderRepository;
import com.tma.vlhau.ecommercefrontend.shipping.ShippingRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ShoppingCartController {

	@Autowired private ShoppingCartService shoppingCartService;
	@Autowired private CustomerService customerService;

	@Autowired private ShippingRateService shippingRateService;

	@Autowired private AddressService addressService;
	@Autowired private CartItemRepository cartItemRepository;
	@GetMapping("/cart")
	public String viewCart(Model model, HttpServletRequest request) throws CustomerNotFoundException {

		Customer customer = getAuthenticatedCustomer(request);
		List<CartItem> cartItems = shoppingCartService.listCartItem(customer);
		
		float estimatedTotal = 0.0F;
		for(CartItem item : cartItems) {
			estimatedTotal+=item.getSubtotal();
		}
		
		Address defaultAddress = addressService.getDefaultAddress(customer);
		ShippingRate shippingRate = null;


		if (defaultAddress != null) {
			shippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);
		}


		boolean isShippingRateIsSupported = (shippingRate != null);
		int amountProduct = cartItemRepository.getamountProductByCustomer(customer);

		model.addAttribute("amountProduct",amountProduct);
		model.addAttribute("shippingSupported", isShippingRateIsSupported);
		model.addAttribute("cartItems",cartItems);
		model.addAttribute("estimatedTotal",estimatedTotal);

		return "cart/shopping_cart";
	}
	
	private Customer getAuthenticatedCustomer(HttpServletRequest request) throws CustomerNotFoundException {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		if (email == null) {
			throw new CustomerNotFoundException("No authenticated customer");
		}

		return customerService.getCustomerByEmail(email);
	}
	
}
