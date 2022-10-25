package com.tma.vlhau.ecommercebackend.shippingrate.service;

import com.tma.vlhau.ecommercebackend.paging.PagingAndSortingHelper;
import com.tma.vlhau.ecommercebackend.product.repository.ProductRepository;
import com.tma.vlhau.ecommercebackend.setting.country.CountryRepository;
import com.tma.vlhau.ecommercebackend.shippingrate.controller.DistrictRepository;
import com.tma.vlhau.ecommercebackend.shippingrate.exception.ShippingRateAlreadyExistsException;
import com.tma.vlhau.ecommercebackend.shippingrate.exception.ShippingRateNotFoundException;
import com.tma.vlhau.ecommercebackend.shippingrate.repository.ShippingRateRepository;
import com.tma.vlhau.ecommercecommon.entity.*;
import com.tma.vlhau.ecommercecommon.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class ShippingRateService {
	public static final int RATES_PER_PAGE = 10;
	private static final float DIM_DIVISOR = (float)3.53060706;	
	@Autowired private ShippingRateRepository shippingRateRepository;
	@Autowired private CountryRepository countryRepo;

	@Autowired private DistrictRepository districtRepository;

	@Autowired private ProductRepository productRepo;

	public List<ShippingRate> listAll() {
		Sort sort = Sort.by("id").ascending();

		return (List<ShippingRate>) shippingRateRepository.findAll(sort);
	}

	public Page<ShippingRate> listByPage(int pageNum,  String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);

		if (sortDir.equals("asc")) {
			sort = sort.ascending();
		} else {
			sort = sort.descending();
		}

		Pageable pageable = PageRequest.of(pageNum - 1, RATES_PER_PAGE, sort);

		if (keyword != null) {
			return shippingRateRepository.findAll(keyword, pageable);
		}

		return shippingRateRepository.findAll(pageable);


	}
	
	public List<Country> listAllCountries() {
		return countryRepo.findAllByOrderByNameAsc();
	}


	
	public void save(ShippingRate shippingRate, Integer districtId) {

		shippingRate.setDistrict(districtRepository.findById(districtId).get());
		shippingRateRepository.save(shippingRate);
	}

	public ShippingRate get(Integer id) throws ShippingRateNotFoundException {
		try {
			return shippingRateRepository.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
		}
	}
	
	public void updateCODSupport(Integer id, boolean codSupported) throws ShippingRateNotFoundException {
		Long count = shippingRateRepository.countById(id);
		if (count == null || count == 0) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
		}

		shippingRateRepository.updateCODSupport(id, codSupported);
	}
	
	public void delete(Integer id) throws ShippingRateNotFoundException {
		Long count = shippingRateRepository.countById(id);
		if (count == null || count == 0) {
			throw new ShippingRateNotFoundException("Could not find shipping rate with ID " + id);
			
		}
		shippingRateRepository.deleteById(id);
	}

	public String checkDistrictUnique(Integer shippingRateId, Integer districtId) {
		District district = districtRepository.findById(districtId).get();
		ShippingRate shippingRate = shippingRateRepository.findByDistrict(district);

		boolean isCreatingNew = (shippingRateId == null || shippingRateId == 0);

		if (isCreatingNew) {
			if (shippingRate != null)
				return "Duplicate shipping rate";
		} else {
			if (shippingRate != null && shippingRate.getId() != shippingRateId)
				return "Duplicate shipping rate";
		}

		return "OK";
	}
	
	
	
	
//	public float calculateShippingCost(Integer productId, Integer countryId, String state)
//			throws ShippingRateNotFoundException {
//		ShippingRate shippingRate = shipRepo.findByCountryAndState(countryId, state);
//
//		if (shippingRate == null) {
//			throw new ShippingRateNotFoundException("No shipping rate found for the given "
//					+ "destination. You have to enter shipping cost manually.");
//		}
//
//		Product product = productRepo.findById(productId).get();
//
//		float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
//		float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;
//
//		return finalWeight * shippingRate.getRate();
//	}
}
