package com.tma.vlhau.ecommercefrontend.order;

import com.tma.vlhau.ecommercecommon.entity.Address;
import com.tma.vlhau.ecommercecommon.entity.CartItem;
import com.tma.vlhau.ecommercecommon.entity.Customer;
import com.tma.vlhau.ecommercecommon.entity.order.*;
import com.tma.vlhau.ecommercecommon.entity.product.Product;
import com.tma.vlhau.ecommercecommon.exception.OrderNotFoundException;
import com.tma.vlhau.ecommercefrontend.checkout.CheckoutInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    public static final int  ORDERS_PER_PAGE = 5;
    @Autowired private OrderRepository orderRepository;

    public Order createOrder(Customer customer, Address address, List<CartItem> cartItems, PaymentMethod paymentMethod, CheckoutInfo checkoutInfo) {
        Order newOrder = new Order();
        newOrder.setOrderTime(new Date());

        newOrder.setCustomer(customer);
        newOrder.setAddress(address);
        newOrder.setProductCost(checkoutInfo.getProductCost());
        newOrder.setSubtotal(checkoutInfo.getProductTotal());
        newOrder.setShippingCost(checkoutInfo.getShippingCostTotal());
        newOrder.setDeliverDays(checkoutInfo.getDeliverDays());
        newOrder.setDeliverDate(checkoutInfo.getDeliverDate());
        newOrder.setPaymentMethod(paymentMethod);
        newOrder.setOrderTime(new java.util.Date());


        Set<OrderDetail> orderDetails = newOrder.getOrderDetails();
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(newOrder);
            orderDetail.setProduct(product);
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setUnitPrice(product.getDiscountPrice());
            orderDetail.setProductCost(product.getCost() * cartItem.getQuantity());
            orderDetail.setSubtotal(cartItem.getSubtotal());
            orderDetail.setShippingCost(cartItem.getShippingCost());

            orderDetails.add(orderDetail);
        }


        OrderTrack track = new OrderTrack();
        track.setOrder(newOrder);
        if (paymentMethod.equals(PaymentMethod.PAYPAL)) {
            track.setStatus(OrderStatus.PAID);
            track.setNotes(OrderStatus.PAID.defaultDescription());
        } else {
            track.setStatus(OrderStatus.NEW);
            track.setNotes(OrderStatus.NEW.defaultDescription());
        }
        track.setUpdatedTime(new Date());

        newOrder.getOrderTracks().add(track);


        return orderRepository.save(newOrder);
    }

    public Page<Order> listForCustomerByPage(Customer customer, int pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);

        if (keyword != null) {
            System.out.println("Keyword: " + keyword);
            System.out.println("ID: " + customer.getId());
            return orderRepository.findAll(keyword, customer.getId(), pageable);
        }

        return orderRepository.findAll(customer.getId(), pageable);
    }

    public Order getOrder(Integer id, Customer customer) {
        return orderRepository.findByIdAndCustomer(id, customer);
    }

    public List<Order> getOrderByCustomer(Customer customer){
        return orderRepository.getOrderByCustomer(customer);
    }

    public void setOrderReturnRequested(OrderReturnRequest request, Customer customer) throws OrderNotFoundException {
        Order order = orderRepository.findByIdAndCustomer(request.getOrderId(), customer);
        System.out.println("Customer: " + customer);
        if (order == null) {
            throw new OrderNotFoundException("Could not find any order with order ID: " + request.getOrderId());
        }

        if (order.isReturnRequested()) return;

        OrderTrack track = new OrderTrack();
        track.setOrder(order);
        track.setUpdatedTime(new Date());
        track.setStatus(OrderStatus.RETURN_REQUESTED);

        String note = "Reason: " + request.getReason();
        if (!"".equals(request.getNote())) {
            note += "." + request.getNote();
        }

        track.setNotes(note);

        order.getOrderTracks().add(track);

        orderRepository.save(order);
    }

    public List<Order> getAll(){
        return orderRepository.findAll();
    }

}
