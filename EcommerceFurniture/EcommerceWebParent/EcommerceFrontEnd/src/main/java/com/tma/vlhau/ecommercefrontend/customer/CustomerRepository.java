package com.tma.vlhau.ecommercefrontend.customer;

import com.tma.vlhau.ecommercecommon.entity.AuthenticationType;
import com.tma.vlhau.ecommercecommon.entity.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {

    @Query("SELECT c FROM Customer c WHERE c.email=?1")
    Customer findByEmail(String email);


    @Query("SELECT c FROM Customer c WHERE c.phoneNumber=?1")
    Customer findByPhoneNumber(String phoneNumber);

    @Query("SELECT c FROM Customer c WHERE c.verificationCode=?1")
    Customer findByVerificationCode(String code);

    @Query("UPDATE Customer c SET c.enabled = true, c.verificationCode = null WHERE c.id=?1")
    @Modifying
    void enabled(Integer id);

    Customer findByResetPasswordToken(String token);

    @Query("UPDATE Customer c SET c.authenticationType = ?2 where c.id=?1")
    @Modifying
    void updateAuthenticationType(Integer customerId, AuthenticationType type);
}
