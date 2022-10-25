package com.tma.vlhau.ecommercecommon.entity.order;

import com.tma.vlhau.ecommercecommon.entity.Category;
import com.tma.vlhau.ecommercecommon.entity.IdBasedEntity;
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
@Table(name = "order_details")
public class OrderDetail extends IdBasedEntity {

		@Column(name = "quantity", nullable = false)
	    private int quantity;
		@Column(name = "productCost", nullable = false)
	    private float productCost;

		@Column(name = "shippingCost", nullable = false)
	    private float shippingCost;
		@Column(name = "productPrice", nullable = false)
	    private float unitPrice;
		@Column(name = "subtotal", nullable = false)
	    private float subtotal;
		@ManyToOne
		@JoinColumn(name = "product_id", nullable = false)
		private Product product;
		
		@ManyToOne
		@JoinColumn(name = "order_id", nullable = false)
		private Order order;

		public OrderDetail() {

		}

	public OrderDetail(String categoryName, int quantity, float productCost, float shippingCost, float subtotal, Product product) {
		this.product = product;
		this.product.setCategory(new Category(categoryName));
		this.quantity = quantity;
		this.productCost = productCost;
		this.shippingCost = shippingCost;
		this.subtotal = subtotal;
	}

	public OrderDetail(int quantity, String productName, float productCost, float shippingCost, float subtotal) {
		this.product = new Product(productName);
		this.quantity = quantity;
		this.productCost = productCost;
		this.shippingCost = shippingCost;
		this.subtotal = subtotal;
	}

}
