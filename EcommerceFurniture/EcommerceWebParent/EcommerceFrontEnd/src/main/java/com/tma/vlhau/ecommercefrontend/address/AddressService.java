package com.tma.vlhau.ecommercefrontend.address;

import com.tma.vlhau.ecommercecommon.entity.Address;
import com.tma.vlhau.ecommercecommon.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AddressService {
	
	@Autowired private AddressRepository repo;

	public Address getAddressById(Integer id){
		return repo.findById(id).get();
	}
	
	public List<Address> listAddressBook(Customer customer) {
		return repo.findByCustomer(customer);
	}

	public void save(Address address) {
		repo.save(address);
	}
	
	public Address get(Integer addressId, Integer customerId) {
		return repo.findByIdAndCustomer(addressId, customerId);
	}
	
	public void delete(Integer addressId, Integer customerId) {
		repo.deleteByIdAndCustomer(addressId, customerId);
	}
	
	public void setDefaultAddress(Integer defaultAddressId, Integer customerId) {
		if (defaultAddressId > 0) {
			repo.setDefaultAddress(defaultAddressId);
		}
		
		repo.setNonDefaultForOthers(defaultAddressId, customerId);
	}
	
	public Address getDefaultAddress(Customer customer) {
		return repo.findDefaultByCustomer(customer.getId());
	}
}
