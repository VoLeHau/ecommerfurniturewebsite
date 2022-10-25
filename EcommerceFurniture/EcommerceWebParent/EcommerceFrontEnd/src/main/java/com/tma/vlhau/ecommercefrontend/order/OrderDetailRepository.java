package com.tma.vlhau.ecommercefrontend.order;

import com.tma.vlhau.ecommercecommon.entity.Customer;
import com.tma.vlhau.ecommercecommon.entity.order.Order;
import com.tma.vlhau.ecommercecommon.entity.order.OrderDetail;
import com.tma.vlhau.ecommercecommon.entity.order.OrderTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail,Integer> {

    @Query("select od from OrderDetail od, Order o, Customer c where od.order=o and o.customer=c and c=?1")
    List<OrderDetail> getOrderDetailByOrder(Customer customer);
}
