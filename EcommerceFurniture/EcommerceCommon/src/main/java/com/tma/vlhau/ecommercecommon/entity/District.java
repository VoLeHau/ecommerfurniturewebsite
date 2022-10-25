package com.tma.vlhau.ecommercecommon.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "districts")
public class District extends IdBasedEntity{

    @Column(nullable = false, length = 45)
    private String name;

    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @OneToMany(mappedBy = "district")
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    Set<Ward> wards = new HashSet<>();

    @OneToMany(mappedBy = "district")
    Set<ShippingRate> shippingRates = new HashSet<>();

    public District(Integer id) {
        this.id = id;
    }


}
