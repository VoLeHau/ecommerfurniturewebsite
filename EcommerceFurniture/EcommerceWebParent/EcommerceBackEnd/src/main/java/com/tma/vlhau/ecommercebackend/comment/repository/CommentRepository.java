package com.tma.vlhau.ecommercebackend.comment.repository;

import com.tma.vlhau.ecommercecommon.entity.Brand;
import com.tma.vlhau.ecommercecommon.entity.Comment;
import com.tma.vlhau.ecommercecommon.entity.Role;
import com.tma.vlhau.ecommercecommon.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CommentRepository extends PagingAndSortingRepository<Comment, Integer> {
    @Query("select c from Comment c where c.product=?1 order by c.date ASC")
    List<Comment> getCommentsByProductAndDateOrderByAsc(Product product);

    @Query("SELECT c FROM Comment c ")
    Page<Comment> findAllBy(Pageable pageable);

    @Query("SELECT c FROM Comment c  WHERE c.text LIKE %?1%")
    Page<Comment> findAllBy(String keyword, Pageable pageable);
}
