package com.tma.vlhau.ecommercebackend.shippingrate.controller;

import com.tma.vlhau.ecommercecommon.entity.City;
import com.tma.vlhau.ecommercecommon.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Integer> {

    List<City> findByCountryOrderByNameAsc(Country country);



}
