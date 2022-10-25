package com.tma.vlhau.ecommercecommon.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "addresses")

public class Address extends IdBasedEntity{

	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;


	@Column(name = "address_detail")
	private String addressDetail;

	@Column(name = "default_address")
	private boolean defaultForShipping;

	@ManyToOne
	@JoinColumn(name = "ward_id", nullable = false)
	private Ward ward;

	public Address(Customer customer, boolean defaultForShipping, Ward ward) {
		this.customer = customer;
		this.defaultForShipping = defaultForShipping;
		this.ward = ward;
	}

	@Override
	public String toString() {
		return "Address{" +
				"id=" + id +
				'}';
	}
}
