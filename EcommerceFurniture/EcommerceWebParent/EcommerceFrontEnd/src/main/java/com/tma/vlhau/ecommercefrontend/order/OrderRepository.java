package com.tma.vlhau.ecommercefrontend.order;

import com.tma.vlhau.ecommercecommon.entity.Customer;
import com.tma.vlhau.ecommercecommon.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query("SELECT o FROM Order o WHERE o.customer.id = ?1")
    Page<Order> findAll(Integer customerId, Pageable pageable);

    Order findByIdAndCustomer(Integer id, Customer customer);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderDetails od JOIN od.product p " +
            "WHERE o.customer.id=?2 AND (p.name LIKE %?1%)")
    Page<Order> findAll(String keyword, Integer customerId, Pageable pageable);


    @Query("SELECT o from Order o where o.customer=?1")
    List<Order> getOrderByCustomer(Customer customer);


}
