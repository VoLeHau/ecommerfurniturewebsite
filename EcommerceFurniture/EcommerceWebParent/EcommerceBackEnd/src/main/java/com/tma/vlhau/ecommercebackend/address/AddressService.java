package com.tma.vlhau.ecommercebackend.address;

import com.tma.vlhau.ecommercecommon.entity.Address;
import com.tma.vlhau.ecommercecommon.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public Address findById(Integer id) {
        return addressRepository.findById(id).get();
    }
    public Address getAddressByCustomer(Customer customer){
        return addressRepository.getAddressByCustomer(customer);
    }

    public List<Address> getListAddressByCustomer(Customer customer){
        return  addressRepository.getListAddressByCustomer(customer);
    }
}
