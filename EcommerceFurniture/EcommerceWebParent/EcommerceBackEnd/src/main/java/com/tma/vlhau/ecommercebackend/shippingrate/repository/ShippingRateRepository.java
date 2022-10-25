package com.tma.vlhau.ecommercebackend.shippingrate.repository;

import com.tma.vlhau.ecommercebackend.paging.SearchRepository;
import com.tma.vlhau.ecommercecommon.entity.Brand;
import com.tma.vlhau.ecommercecommon.entity.District;
import com.tma.vlhau.ecommercecommon.entity.ShippingRate;
import com.tma.vlhau.ecommercecommon.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ShippingRateRepository extends PagingAndSortingRepository<ShippingRate, Integer> {
	

	@Query("select sr from ShippingRate sr where sr.district = ?1")
	ShippingRate findByDistrict(District district);

	@Query("UPDATE ShippingRate sr SET sr.codSupported = ?2 WHERE sr.id = ?1")
	@Modifying
	public void updateCODSupport(Integer id, boolean enabled);

	@Query("SELECT sr FROM ShippingRate sr WHERE CONCAT(sr.id, ' ', sr.rate, ' ' ,sr.days) LIKE %?1%")
	Page<ShippingRate> findAll(String keyword, Pageable pageable);

	Long countById(Integer id);

}
