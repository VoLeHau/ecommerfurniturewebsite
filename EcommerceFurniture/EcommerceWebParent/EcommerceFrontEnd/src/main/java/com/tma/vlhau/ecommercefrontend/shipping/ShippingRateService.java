package com.tma.vlhau.ecommercefrontend.shipping;

import com.tma.vlhau.ecommercecommon.entity.Address;
import com.tma.vlhau.ecommercecommon.entity.Customer;
import com.tma.vlhau.ecommercecommon.entity.ShippingRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingRateService {

    @Autowired
    private ShippingRateRepository shippingRateRepository;

    public ShippingRate getShippingRateForAddress(Address address) {

        return shippingRateRepository.getShippingRateByDistrict(address.getWard().getDistrict());
    }

}
