package com.tma.vlhau.ecommercefrontend.shipping;

import com.tma.vlhau.ecommercecommon.entity.Country;
import com.tma.vlhau.ecommercecommon.entity.District;
import com.tma.vlhau.ecommercecommon.entity.ShippingRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ShippingRateRepository extends JpaRepository<ShippingRate, Integer> {


    @Query("select sr from ShippingRate sr where sr.district=?1")
    ShippingRate getShippingRateByDistrict(District district);

}
