package com.tma.vlhau.ecommercefrontend.setting;

import com.tma.vlhau.ecommercecommon.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DistrictRestController {

    @Autowired
    private DistrictRepository districtRepository;

    @GetMapping("/settings/list_districts_by_city/{id}")
    public List<DistrictDTO> listByCountry(@PathVariable("id") Integer cityId) {

        List<District> listDistricts= districtRepository.findByDistrictOrderByNameAsc(new City(cityId));

        List<DistrictDTO> result = new ArrayList<>();

        for  (District district : listDistricts) {
            result.add(new DistrictDTO(district.getId(), district.getName()));
        }
        return result;
    }
}
