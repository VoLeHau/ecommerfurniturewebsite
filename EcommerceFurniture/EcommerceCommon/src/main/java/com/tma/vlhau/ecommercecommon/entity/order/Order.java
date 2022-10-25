package com.tma.vlhau.ecommercecommon.entity.order;

import com.tma.vlhau.ecommercecommon.entity.Address;
import com.tma.vlhau.ecommercecommon.entity.Customer;
import com.tma.vlhau.ecommercecommon.entity.IdBasedEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order extends IdBasedEntity {
	@Column(name = "order_time")
	private Date orderTime;

	@Column(name = "shipping_cost")
	private float shippingCost;
	@Column(name = "product_cost")
	private float productCost;

	@Column(name = "subtotal")
	private float subtotal;
	@Column(name = "deliver_days")
	private int deliverDays;
	@Column(name = "deliver_date")
	private Date deliverDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_method")
	private PaymentMethod paymentMethod;

//	@Enumerated(EnumType.STRING)
//	private OrderStatus status;

	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;

	@ManyToOne
	@JoinColumn(name = "address_id", nullable = false)
	private Address address;


	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OrderDetail> orderDetails = new HashSet<>();

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("updatedTime ASC")
	private List<OrderTrack> orderTracks = new ArrayList<>();

	public Order() {

	}

	public Order(Integer id, Date orderTime, float productCost, float subtotal) {
		this.id = id;
		this.orderTime = orderTime;
		this.productCost = productCost;
		this.subtotal = subtotal;
	}


	@Transient
	public String getShippingAddress() {
		String address = customer.getFirstName() + customer.getLastName();
		return address;
	}


	public List<OrderTrack> getOrderTracks() {
		return orderTracks;
	}

	public void setOrderTracks(List<OrderTrack> orderTracks) {
		this.orderTracks = orderTracks;
	}

	@Transient
	public String getDeliverDateOnForm() {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormatter.format(this.deliverDate);
	}

	public void setDeliverDateOnForm(String dateString) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

		try {
			this.deliverDate = dateFormatter.parse(dateString);
		} catch (ParseException ex) {
			ex.printStackTrace();
		}
	}

	@Transient
	public String getRecipientName() {
		String name = customer.getFirstName() + " " + customer.getLastName();
		return name;
	}

	@Transient
	public String getRecipientAddress() {
		String name = address.getAddressDetail() + ", " + address.getWard().getName() +
		", " + address.getWard().getDistrict().getName() + ", " + address.getWard().getDistrict().getCity().getName() +
		", " + address.getWard().getDistrict().getCity().getCountry().getName();
		return name;
	}


	@Transient
	public boolean isCOD() {
		return paymentMethod.equals(PaymentMethod.COD);
	}

	@Transient
	public boolean isProcessing() {
		return hasStatus(OrderStatus.PROCESSING);
	}

	@Transient
	public boolean isPicked() {
		return hasStatus(OrderStatus.PICKED);
	}

	@Transient
	public boolean isShipping() {
		return hasStatus(OrderStatus.SHIPPING);
	}

	@Transient
	public boolean isDelivered() {
		return hasStatus(OrderStatus.DELIVERED);
	}

	@Transient
	public boolean isReturnRequested() {
		return hasStatus(OrderStatus.RETURN_REQUESTED);
	}

	@Transient
	public boolean isReturned() {
		return hasStatus(OrderStatus.RETURNED);
	}

	public boolean hasStatus(OrderStatus status) {
		for (OrderTrack aTrack : orderTracks) {
			if (aTrack.getStatus().equals(status)) {
				return true;
			}
		}

		return false;
	}

	@Transient
	public String getProductNames() {
		String productName = "";

		productName = "<ul>";

		for (OrderDetail detail : orderDetails) {
			productName += "<li>" + detail.getProduct().getShortName() + "</li>";
		}

		productName += "</ul>";

		return productName;
	}
}
