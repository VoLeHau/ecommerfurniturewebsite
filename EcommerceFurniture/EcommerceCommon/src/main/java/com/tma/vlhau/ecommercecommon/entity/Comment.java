package com.tma.vlhau.ecommercecommon.entity;

import com.tma.vlhau.ecommercecommon.entity.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "commnents")
public class Comment extends IdBasedEntity{

    @Column(name = "text", nullable = true, length = 1024)
    private String text;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @Column(name = "rating", nullable = false)
    private int rating;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

}
