package com.tma.vlhau.ecommercefrontend.order;

import com.tma.vlhau.ecommercecommon.entity.Customer;
import com.tma.vlhau.ecommercecommon.entity.order.Order;
import com.tma.vlhau.ecommercecommon.entity.order.OrderDetail;
import com.tma.vlhau.ecommercecommon.entity.order.OrderTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    List<OrderDetail> getOrderDetailByOrder(Customer customer){
        return orderDetailRepository.getOrderDetailByOrder(customer);
    }
}
