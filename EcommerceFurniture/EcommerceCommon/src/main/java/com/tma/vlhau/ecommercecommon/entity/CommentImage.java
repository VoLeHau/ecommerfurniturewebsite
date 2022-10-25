package com.tma.vlhau.ecommercecommon.entity;

import com.tma.vlhau.ecommercecommon.entity.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "commnent_images")
public class CommentImage extends IdBasedEntity{

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @Column(name = "image", nullable = false)
    private String image;

    public String getImagePath() {
        if (id == null || image == null) return "";
        return "/comment-images/" + this.id + "/" + this.image;
    }

}
