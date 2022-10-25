package com.tma.vlhau.ecommercebackend.setting.city;

import com.tma.vlhau.ecommercebackend.shippingrate.controller.CityRepository;
import com.tma.vlhau.ecommercecommon.entity.Country;
import com.tma.vlhau.ecommercecommon.entity.City;
import com.tma.vlhau.ecommercecommon.entity.CityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CityController {

    @Autowired
    private CityRepository cityRepository;

    @GetMapping("/cities/list_by_country/{id}")
    public List<CityDTO> listByCountry(@PathVariable("id") Integer countryId) {
        List<City> listCities = cityRepository.findByCountryOrderByNameAsc(new Country(countryId));

        List<CityDTO> result = new ArrayList<>();

        for  (City city : listCities) {
            result.add(new CityDTO(city.getId(), city.getName()));
        }
        return result;
    }

    @PostMapping("/cities/save")
    public String save(@RequestBody City city) {
        City savedCity = cityRepository.save(city);
        return String.valueOf(savedCity.getId());
    }

    @DeleteMapping("/cities/delete/{id}")
    public void delete(@PathVariable("id") Integer id) {
        cityRepository.deleteById(id);
    }

}
