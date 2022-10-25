package com.tma.vlhau.ecommercefrontend.address;

import com.tma.vlhau.ecommercecommon.entity.*;
import com.tma.vlhau.ecommercefrontend.ControllerHelper;
import com.tma.vlhau.ecommercefrontend.Utility;
import com.tma.vlhau.ecommercefrontend.customer.CustomerService;
import com.tma.vlhau.ecommercefrontend.order.OrderRepository;
import com.tma.vlhau.ecommercefrontend.setting.*;
import com.tma.vlhau.ecommercefrontend.shoppingcart.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AddressController {

	@Autowired private AddressService addressService;
	@Autowired private CustomerService customerService;
	@Autowired private ControllerHelper controllerHelper;


	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private DistrictRepository districtRepository;

	@Autowired
	private WardRepository wardRepository;
	@Autowired private CartItemRepository cartItemRepository;
	@GetMapping("/address_book")
	public String showAddressBook(Model model, HttpServletRequest request) {
		Customer customer = controllerHelper.getAuthenticatedCustomer(request);
		List<Address> listAddresses = addressService.listAddressBook(customer);

		boolean usePrimaryAddressAsDefault = false;
		for (Address address : listAddresses) {
			usePrimaryAddressAsDefault = address.isDefaultForShipping();
			break;
		}

		int amountProduct= cartItemRepository.getamountProductByCustomer(customer);

		model.addAttribute("amountProduct",amountProduct);

		model.addAttribute("listAddresses", listAddresses);
		model.addAttribute("customer", customer);
		model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);

		return "address_book/addresses";
	}
	
	@GetMapping("/address_book/new")
	public String newAddress(Model model,HttpServletRequest request) {
		Customer customer = controllerHelper.getAuthenticatedCustomer(request);
		model.addAttribute("listCountries", countryRepository.findAll());
		model.addAttribute("address", new Address());
		model.addAttribute("pageTitle", "Add New Address");
		int amountProduct = cartItemRepository.getamountProductByCustomer(customer);

		model.addAttribute("amountProduct",amountProduct);
		return "address_book/address_form";
	}
	
	@PostMapping("/address_book/save")
	public String saveAddress(Address address, HttpServletRequest request, RedirectAttributes ra, @RequestParam(value = "ward") int wardId,
							  @RequestParam(value = "addressDetail") String addressDetail) {

		Customer customer = controllerHelper.getAuthenticatedCustomer(request);

		address.setWard(wardRepository.findById(wardId).get());
		address.setAddressDetail(addressDetail);
		address.setCustomer(customer);
		address.setDefaultForShipping(address.isDefaultForShipping());
		addressService.save(address);

		String redirectOption = request.getParameter("redirect");
		String redirectURL = "redirect:/address_book";

		if ("checkout".equals(redirectOption)) {
			redirectURL += "?redirect=checkout";
		} else if ("cart".equals(redirectOption)) {
			redirectURL += "?redirect=cart";
		}

		ra.addFlashAttribute("message", "The address has been saved successfully.");

		return redirectURL;
	}
	
	@GetMapping("/address_book/edit/{id}")
	public String editAddress(@PathVariable("id") Integer addressId, Model model,
			HttpServletRequest request) {
		Customer customer = controllerHelper.getAuthenticatedCustomer(request);

		List<Country> listCountries = countryRepository.findAll();

		Address address = addressService.get(addressId, customer.getId());

		Country country = address.getWard().getDistrict().getCity().getCountry();

		List<City> listCities = cityRepository.findByCountryOrderByNameAsc(country);

		City city = address.getWard().getDistrict().getCity();
		List<District> listDistricts = districtRepository.findByDistrictOrderByNameAsc(city);

		District district = address.getWard().getDistrict();
		List<Ward> listWards = wardRepository.findByDistrictOrderByNameAsc(district);
		int amountProduct = cartItemRepository.getamountProductByCustomer(customer);

		model.addAttribute("amountProduct",amountProduct);

		model.addAttribute("address", address);
		model.addAttribute("listCountries", listCountries);
		model.addAttribute("listCities", listCities);
		model.addAttribute("listDistricts", listDistricts);
		model.addAttribute("listWards", listWards);

		model.addAttribute("pageTitle", "Edit Address (ID: " + addressId + ")");

		
		return "address_book/address_form";
	}
	
	@GetMapping("/address_book/delete/{id}")
	public String deleteAddress(@PathVariable("id") Integer addressId, RedirectAttributes ra,
			HttpServletRequest request) {
		Customer customer = controllerHelper.getAuthenticatedCustomer(request);
		addressService.delete(addressId, customer.getId());
		
		ra.addFlashAttribute("message", "The address ID " + addressId + " has been deleted.");
		
		return "redirect:/address_book";
	}
	
	@GetMapping("/address_book/default/{id}")
	public String setDefaultAddress(@PathVariable("id") Integer addressId,
			HttpServletRequest request) {

		Customer customer = controllerHelper.getAuthenticatedCustomer(request);
		addressService.setDefaultAddress(addressId, customer.getId());
		
		String redirectOption = request.getParameter("redirect");
		String redirectURL = "redirect:/address_book";
		
		if ("cart".equals(redirectOption)) {
			redirectURL = "redirect:/cart";
		} else if ("checkout".equals(redirectOption)) {
			redirectURL = "redirect:/checkout";
		}
		
		return redirectURL; 
	}
}

