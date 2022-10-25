package com.tma.vlhau.ecommercecommon.entity;


import com.tma.vlhau.ecommercecommon.entity.validate.Image.ValidImage;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 128, nullable = false, unique = true)
    @NotBlank(message = "Please enter category name")
    @Size(min = 2, max = 64 , message = "Size must be between 2 and 64")
    private String name;


    @Column(length = 64, nullable = false, unique = true)
    @NotBlank(message = "Please enter category alias")
    @Size(min = 2, max = 64 , message = "Size must be between 2 and 64")
    private String alias;
    @Column(length = 128, nullable = false)
    private String image;

    @Column(name = "enabled")
    private boolean enabled=true;

    @Column(name = "all_parent_ids", length = 256, nullable = true)
    private String allParentIDs;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @OrderBy("name asc")
    private Set<Category> children = new HashSet<>();

    public static Category copyFull(Category category) {
        return Category.builder()
                        .name(category.getName())
                        .id(category.getId())
                        .alias(category.getAlias())
                        .image(category.getImage())
                        .parent(category.getParent())
                        .children(category.getChildren())
                        .enabled(category.isEnabled())
                        .hasChildren(category.getChildren().size() > 0)
                        .build();
    }

    public static Category copyFullWithFullName(Category category, String newName) {
        Category copyCategory = copyFull(category);
        copyCategory.setName(newName);

        return copyCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Category category = (Category) o;
        return id != null && Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public String getImagePath() {
        if (id==null || image == null) return "/images/default.png";
        return "/category-images/" + this.id + "/" + this.image;
    }

    @Override
    public String toString() {
        return name;
    }

    @Transient
    private boolean hasChildren;

    public Category(String name) {
        this.name = name;
    }
}
