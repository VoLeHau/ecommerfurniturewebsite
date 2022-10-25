package com.tma.vlhau.ecommercecommon.entity;


import com.tma.vlhau.ecommercecommon.entity.product.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Entity
@Table(name="cart_items")
public class CartItem extends IdBasedEntity {
	
	@ManyToOne
	@JoinColumn(name="customer_id", nullable = false)
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name="product_id", nullable = false)
	private Product product;

	private int quantity;

	@Transient
	private float shippingCost;

	public CartItem() {
	}

	@Override
	public String toString() {
		return "CartItem [id=" + id + ", customer=" + customer.getFullName() + ", product=" + product.getShortName() + ", quantity=" + quantity
				+ "]";
	}
	
	@Transient
	public float getSubtotal() {
		return (float) (product.getDiscountPrice() * quantity);
	}

	@Transient
	public float getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(float shippingCost) {
		this.shippingCost = shippingCost;
	}
}
