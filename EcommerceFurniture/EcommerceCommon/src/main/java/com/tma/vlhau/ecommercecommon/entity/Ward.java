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
@Table(name = "wards")
public class Ward extends IdBasedEntity{

    @Column(nullable = false, length = 45)
    private String name;

    @ManyToOne
    @JoinColumn(name = "district_id",nullable = false)
    private District district;

    @OneToMany(mappedBy = "ward")
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    Set<Address> addresses = new HashSet<>();

    public Ward(Integer id) {
        this.id = id;
    }


}
