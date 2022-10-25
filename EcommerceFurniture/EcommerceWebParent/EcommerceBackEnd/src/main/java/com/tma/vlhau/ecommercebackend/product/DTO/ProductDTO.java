package com.tma.vlhau.ecommercebackend.product.DTO;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class ProductDTO {
	private String name;
	private String imagePath;
	private double price;
	private double cost;


}
