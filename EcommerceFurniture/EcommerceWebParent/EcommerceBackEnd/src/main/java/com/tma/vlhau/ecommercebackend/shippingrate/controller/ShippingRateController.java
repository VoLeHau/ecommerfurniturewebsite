package com.tma.vlhau.ecommercebackend.shippingrate.controller;

import com.tma.vlhau.ecommercebackend.brand.service.BrandService;
import com.tma.vlhau.ecommercebackend.paging.PagingAndSortingHelper;
import com.tma.vlhau.ecommercebackend.paging.PagingAndSortingParam;
import com.tma.vlhau.ecommercebackend.setting.country.CountryRepository;
import com.tma.vlhau.ecommercebackend.shippingrate.exception.ShippingRateAlreadyExistsException;
import com.tma.vlhau.ecommercebackend.shippingrate.exception.ShippingRateNotFoundException;
import com.tma.vlhau.ecommercebackend.shippingrate.service.ShippingRateService;
import com.tma.vlhau.ecommercecommon.entity.Brand;
import com.tma.vlhau.ecommercecommon.entity.Country;
import com.tma.vlhau.ecommercecommon.entity.ShippingRate;
import com.tma.vlhau.ecommercecommon.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ShippingRateController {
	private String defaultRedirectURL = "redirect:/shipping_rates";
	
	@Autowired private ShippingRateService shippingRateService;

	@Autowired
	private CountryRepository countryRepository;
	
	@GetMapping("/shipping_rates")
	public String listFirstPage(@Param("sortDir") String sortDir, Model model) {
		return listByPage(model, 1, "id", "asc", null);
	}
	
	@GetMapping("/shipping_rates/page/{pageNum}")
	public String listByPage(Model model, @PathVariable(name = "pageNum") int pageNum,
							 @Param("sortField") String sortField,
							 @Param("sortDir") String sortDir,
							 @Param("keyword") String keyword){

		if (sortDir == null || sortDir.isEmpty()) {
			sortDir = "asc";
		}

		Page<ShippingRate> listShippingRates = shippingRateService.listByPage(pageNum, sortField, sortDir, keyword);
		List<ShippingRate> listBrands = listShippingRates.getContent();

		long startCount = (long) (pageNum - 1) * ShippingRateService.RATES_PER_PAGE + 1;
		long endCount = startCount + ShippingRateService.RATES_PER_PAGE - 1;

		if (endCount > listShippingRates.getTotalElements()) {
			endCount = listShippingRates.getTotalElements();
		}

		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

		model.addAttribute("sortOrder", sortDir);
		model.addAttribute("reverseSortOrder", reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("listBrands", listBrands);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalItems", listShippingRates.getTotalElements());
		model.addAttribute("totalPages", listShippingRates.getTotalPages());
		model.addAttribute("sortField", sortField);
		model.addAttribute("listShippingRates",listShippingRates);
		model.addAttribute("module", "shipping_rates");
		return "shipping_rates/shipping_rates";
	}
	
	@GetMapping("/shipping_rates/new")
	public String newRate(Model model) {

		model.addAttribute("rate", new ShippingRate());
		model.addAttribute("listCountries", countryRepository.findAll());
		model.addAttribute("pageTitle", "New Shipping Rate");
		
		return "shipping_rates/shipping_rate_form";		
	}

	@PostMapping("/shipping_rates/save")
	public String saveRate(ShippingRate shippingRate, @RequestParam(value = "district") Integer districtId, RedirectAttributes redirectAttributes) {

		shippingRateService.save(shippingRate,districtId);
		redirectAttributes.addAttribute("message", "The shipping rate has been saved successfully.");

		return defaultRedirectURL;
	}
		
	@GetMapping("/shipping_rates/edit/{id}")
	public String editRate(@PathVariable(name = "id") Integer id,
			Model model, RedirectAttributes ra) {
		try {
			ShippingRate rate = shippingRateService.get(id);
			List<Country> listCountries = shippingRateService.listAllCountries();
			
			model.addAttribute("listCountries", listCountries);			
			model.addAttribute("rate", rate);
			model.addAttribute("pageTitle", "Edit Rate (ID: " + id + ")");
			
			return "shipping_rates/shipping_rate_form";
		} catch (ShippingRateNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
			return defaultRedirectURL;
		}
	}

	@GetMapping("/shipping_rates/cod/{id}/enabled/{supported}")
	public String updateCODSupport(@PathVariable(name = "id") Integer id,
			@PathVariable(name = "supported") Boolean supported,
			Model model, RedirectAttributes ra) {
		try {
			shippingRateService.updateCODSupport(id, supported);
			ra.addFlashAttribute("message", "COD support for shipping rate ID " + id + " has been updated.");
		} catch (ShippingRateNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}
		return defaultRedirectURL;
	}
	
	@GetMapping("/shipping_rates/delete/{id}")
	public String deleteRate(@PathVariable(name = "id") Integer id,
			Model model, RedirectAttributes ra) {
		try {
			shippingRateService.delete(id);
			ra.addFlashAttribute("message", "The shipping rate ID " + id + " has been deleted.");
		} catch (ShippingRateNotFoundException ex) {
			ra.addFlashAttribute("message", ex.getMessage());
		}
		return defaultRedirectURL;
	}	
}
