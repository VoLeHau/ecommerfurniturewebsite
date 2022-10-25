package com.tma.vlhau.ecommercecommon.entity.product;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "product_images")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
public class ProductImage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public ProductImage(String name, Product product) {
        this.name = name;
        this.product = product;
    }

    @Transient
    public String getImagePath() {
        return "/product-images/" + product.getId() + "/extras/" + this.name;
    }
}
