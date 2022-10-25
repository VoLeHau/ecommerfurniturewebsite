package com.tma.vlhau.ecommercecommon.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "brands")
public class Brand  {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Integer id;
	
    @Column(name = "name", length = 45, nullable = false, unique = true)
    @NotBlank(message = "Please enter brand name")
    @Size(min = 2, max = 64 , message = "Size must be between 2 and 64")
    private String name;

    @Column(name = "logo", length = 128, nullable = false)
    private String logo;

    @ManyToMany
    @JoinTable(
            name = "brand_categories",
            joinColumns = @JoinColumn(name = "brand_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    public void addCategory(Category category) {
        this.categories.add(category);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Brand brand = (Brand) o;
        return Objects.equals(id, brand.id) && Objects.equals(name, brand.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Transient
    public String getLogoPath() {
        if (id==null || logo == null) return "/images/brand-logo.png";
        return "/brand-logos/" + this.id + "/" + this.logo;
    }

    @Override
    public String toString() {
        return name;
    }

    public Brand(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
