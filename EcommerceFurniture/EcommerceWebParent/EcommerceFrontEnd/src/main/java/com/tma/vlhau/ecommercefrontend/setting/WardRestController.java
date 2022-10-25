package com.tma.vlhau.ecommercefrontend.setting;

import com.tma.vlhau.ecommercecommon.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class WardRestController {

    @Autowired
    private WardRepository wardRepository;

    @GetMapping("/settings/list_wards_by_district/{id}")
    public List<WardDTO> listByDistrict(@PathVariable("id") Integer districtId) {

        List<Ward> listWards= wardRepository.findByDistrictOrderByNameAsc(new District(districtId));

        List<WardDTO> result = new ArrayList<>();

        for  (Ward ward : listWards) {
            result.add(new WardDTO(ward.getId(), ward.getName()));
        }
        return result;
    }
}
