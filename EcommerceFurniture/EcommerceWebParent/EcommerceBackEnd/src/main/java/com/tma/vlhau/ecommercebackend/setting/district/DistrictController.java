package com.tma.vlhau.ecommercebackend.setting.district;

import com.tma.vlhau.ecommercebackend.shippingrate.controller.DistrictRepository;
import com.tma.vlhau.ecommercecommon.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DistrictController {

    @Autowired
    private DistrictRepository districtRepository;

    @GetMapping("/districts/list_by_city/{id}")
    public List<DistrictDTO> listByCountry(@PathVariable("id") Integer cityId) {
        List<District> listDistricts = districtRepository.findByDistrictOrderByNameAsc(new City(cityId));

        List<DistrictDTO> result = new ArrayList<>();

        for  (District district : listDistricts) {
            result.add(new DistrictDTO(district.getId(), district.getName()));
        }
        return result;
    }

    @PostMapping("/districts/save")
    public String save(@RequestBody District district) {
        District savedDistrict = districtRepository.save(district);
        return String.valueOf(savedDistrict.getId());
    }

    @DeleteMapping("/districts/delete/{id}")
    public void delete(@PathVariable("id") Integer id) {
        districtRepository.deleteById(id);
    }

}
