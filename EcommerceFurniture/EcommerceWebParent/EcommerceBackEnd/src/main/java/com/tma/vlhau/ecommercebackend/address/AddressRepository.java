package com.tma.vlhau.ecommercebackend.address;

import com.tma.vlhau.ecommercecommon.entity.Address;
import com.tma.vlhau.ecommercecommon.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {

    @Query("select a from Address a where a.customer=?1 and a.defaultForShipping=true")
    Address getAddressByCustomer(Customer customer);

    @Query("select a from Address  a where a.customer=?1")
    List<Address> getListAddressByCustomer(Customer customer);


}
