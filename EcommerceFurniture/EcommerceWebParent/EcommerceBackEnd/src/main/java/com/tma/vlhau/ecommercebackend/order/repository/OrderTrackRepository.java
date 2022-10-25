package com.tma.vlhau.ecommercebackend.order.repository;

import com.tma.vlhau.ecommercecommon.entity.order.Order;
import com.tma.vlhau.ecommercecommon.entity.order.OrderTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderTrackRepository extends JpaRepository<OrderTrack, Integer> {

    @Query("select ot from OrderTrack ot where ot.order=?1")
    List<OrderTrack> getOrderTrackByOrder(Order order);
}
