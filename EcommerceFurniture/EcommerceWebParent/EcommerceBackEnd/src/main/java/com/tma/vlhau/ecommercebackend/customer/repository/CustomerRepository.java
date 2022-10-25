package com.tma.vlhau.ecommercebackend.customer.repository;

import com.tma.vlhau.ecommercecommon.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CustomerRepository extends PagingAndSortingRepository<Customer, Integer> {

    @Query("SELECT c FROM Customer c WHERE c.email = ?1")
    Customer findByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE CONCAT(c.email, ' ', c.firstName, ' ', c.lastName) " +
            "LIKE %?1%")
    Page<Customer> findAll(String keyword, Pageable pageable);

    Long countById(Integer id);

    @Modifying
    @Query("UPDATE Customer c SET c.enabled= :enabled WHERE c.id = :id")
    void updateEnabledStatus(Integer id, boolean enabled);

    List<Customer> findAll();
}

