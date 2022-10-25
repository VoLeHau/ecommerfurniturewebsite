package com.tma.vlhau.ecommercebackend.order.repository;

import com.tma.vlhau.ecommercebackend.paging.SearchRepository;
import com.tma.vlhau.ecommercecommon.entity.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends SearchRepository<Order, Integer> {
	
	@Query("SELECT o FROM Order o WHERE CONCAT('#', o.id) LIKE %?1% OR "
//			+ " o.paymentMethod LIKE %?1% OR o.status LIKE %?1% OR"
			+ " o.customer.firstName LIKE %?1% OR"
			+ " o.customer.lastName LIKE %?1%")
	public Page<Order> findAll(String keyword, Pageable pageable);
	
	
	public Long countById(Integer id);

	@Query("SELECT o FROM Order o WHERE"
			+ " o.orderTime BETWEEN ?1 AND ?2 ORDER BY o.orderTime ASC")
	List<Order> findByOrderTimeBetween(Date startTime, Date endTime);
}
