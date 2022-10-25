package com.tma.vlhau.ecommercefrontend.setting;

import com.tma.vlhau.ecommercecommon.entity.City;
import com.tma.vlhau.ecommercecommon.entity.District;
import com.tma.vlhau.ecommercecommon.entity.Ward;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WardRepository extends JpaRepository<Ward, Integer> {

    @Query("select w from Ward w where w.district=?1")
    List<Ward> findByDistrictOrderByNameAsc(District district);
}
