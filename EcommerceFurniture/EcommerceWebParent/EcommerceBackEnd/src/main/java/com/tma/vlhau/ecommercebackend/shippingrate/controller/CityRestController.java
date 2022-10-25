package com.tma.vlhau.ecommercebackend.shippingrate.controller;

import com.tma.vlhau.ecommercecommon.entity.City;
import com.tma.vlhau.ecommercecommon.entity.CityDTO;
import com.tma.vlhau.ecommercecommon.entity.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CityRestController {

    @Autowired
    private CityRepository cityRepository;

    @GetMapping("/settings/list_cities_by_country/{id}")
    public List<CityDTO> listByCountry(@PathVariable("id") Integer countryId) {

        List<City> listCities = cityRepository.findByCountryOrderByNameAsc(new Country(countryId));

        List<CityDTO> result = new ArrayList<>();

        for  (City city : listCities) {
            result.add(new CityDTO(city.getId(), city.getName()));
        }
        return result;
    }

}
