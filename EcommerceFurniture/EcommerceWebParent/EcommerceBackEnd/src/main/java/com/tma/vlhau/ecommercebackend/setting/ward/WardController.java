package com.tma.vlhau.ecommercebackend.setting.ward;

import com.tma.vlhau.ecommercecommon.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class WardController {

    @Autowired
    private WardRepository wardRepository;

    @GetMapping("/wards/list_by_district/{id}")
    public List<WardDTO> listByCountry(@PathVariable("id") Integer districtId) {
        List<Ward> listWards = wardRepository.findByDistrictOrderByNameAsc(new District(districtId));

        List<WardDTO> result = new ArrayList<>();

        for  (Ward ward : listWards) {
            result.add(new WardDTO(ward.getId(), ward.getName()));
        }
        return result;
    }

    @PostMapping("/wards/save")
    public String save(@RequestBody Ward ward) {
        Ward savedWard = wardRepository.save(ward);
        return String.valueOf(savedWard.getId());
    }

    @DeleteMapping("/wards/delete/{id}")
    public void delete(@PathVariable("id") Integer id) {
        wardRepository.deleteById(id);
    }

}
