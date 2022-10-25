package com.tma.vlhau.ecommercebackend.shippingrate.controller;

import com.tma.vlhau.ecommercebackend.shippingrate.exception.ShippingRateNotFoundException;
import com.tma.vlhau.ecommercebackend.shippingrate.service.ShippingRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShippingRateRestController {

	@Autowired private ShippingRateService shippingRateService;
	
//	@PostMapping("/get_shipping_cost")
//	public String getShippingCost(Integer productId, Integer countryId, String state)
//			throws ShippingRateNotFoundException {
//		float shippingCost = service.calculateShippingCost(productId, countryId, state);
//		return String.valueOf(shippingCost);
//	}

	@PostMapping("/shipping_rates/check_shipping_rates")
	public String checkDistrictUnique(Integer shippingRateId, Integer districtId) {
		return shippingRateService.checkDistrictUnique(shippingRateId, districtId);
	}

}

