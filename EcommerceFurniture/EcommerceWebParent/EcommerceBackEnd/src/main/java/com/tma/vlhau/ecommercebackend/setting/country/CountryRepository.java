package com.tma.vlhau.ecommercebackend.setting.country;

import com.tma.vlhau.ecommercecommon.entity.Country;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CountryRepository extends CrudRepository<Country, Integer> {

    List<Country> findAllByOrderByNameAsc();

}
