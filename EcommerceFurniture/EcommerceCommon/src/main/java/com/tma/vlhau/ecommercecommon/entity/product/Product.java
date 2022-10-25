package com.tma.vlhau.ecommercecommon.entity.product;

import com.tma.vlhau.ecommercecommon.entity.Brand;
import com.tma.vlhau.ecommercecommon.entity.Category;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Integer id;
	
    @Column(length = 256, nullable = false, unique = true)
    @NotBlank(message = "Please enter product name")
    @Size(min = 2, max = 64 , message = "Size must be between 2 and 64")
    private String name;
    @Column(length = 256, nullable = false, unique = true)
    @NotBlank(message = "Please enter alias")
    @Size(min = 2, max = 64 , message = "Size must be between 2 and 64")
    private String alias;
    @Column(length = 512, nullable = true, name = "short_description")
    private String shortDescription;
    @Column(length = 4096, nullable = true, name = "full_description")
    private String fullDescription;

    @Column(name = "created_time")
    private Date createdTime;
    @Column(name = "updated_time")
    private Date updatedTime;

    @Column(name = "enabled")
    private boolean enabled=true;

    @Column(name = "quantity_in_stock")
    private int quantityInStock;

    @Column(name = "cost")
    private float cost;

    @Column(name = "price")

    private float price;
    @Column(name = "discount_percent")
    private float discountPercent;

    @Column(name = "length")
    private float length;

    @Column(name = "width")
    private float width;

    @Column(name = "height")
    private float height;

    @Column(name = "weight")
    private float weight;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @Column(name = "main_image", nullable = false)
    private String mainImage;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ProductImage> images = new HashSet<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductDetail> details = new ArrayList<>();

    @Transient
    public String getImagePath() {
        return "/images/product-image.png";
    }

    public void addExtraImage(String imageName) {
        this.images.add(new ProductImage(imageName, this));
    }

    public void addDetail(String detailName, String detailValue) {
        this.details.add(new ProductDetail(detailName, detailValue, this));
    }

    public void addDetail(Integer id ,String detailName, String detailValue) {
        this.details.add(new ProductDetail(id, detailName, detailValue, this));
    }

    @Transient
    public String getMainImagePath() {
        if ( mainImage == null || id == null || "".equals(mainImage)) return "/images/product-image.png";

        return "/product-images/" + this.id + "/" + this.mainImage;
    }

    @Transient
    public float getDiscountPrice() {
        if (discountPercent > 0) return price - (price * discountPercent/100);
        return price;
    }

    @Transient
    public String getShortName() {
        if (name.length() > 70) {
            return name.substring(0, 70).concat("...");
        }
        return name;
    }

    public boolean containsImageName(String imageName) {
        Iterator<ProductImage> iterator = images.iterator();

        while (iterator.hasNext()) {
            ProductImage image = iterator.next();
            if (image.getName().equals(imageName)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Product product = (Product) o;
        return id != null && Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

	public Product(Integer id) {
		this.id = id;
	}

    public Product(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", fullDescription='" + fullDescription + '\'' +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                ", enabled=" + enabled +
                ", quantityInStock=" + quantityInStock +
                ", cost=" + cost +
                ", price=" + price +
                ", discountPercent=" + discountPercent +
                ", length=" + length +
                ", width=" + width +
                ", height=" + height +
                ", weight=" + weight +
                ", category=" + category +
                ", brand=" + brand +
                ", mainImage='" + mainImage + '\'' +
                ", images=" + images +
                ", details=" + details +
                '}';
    }
}
