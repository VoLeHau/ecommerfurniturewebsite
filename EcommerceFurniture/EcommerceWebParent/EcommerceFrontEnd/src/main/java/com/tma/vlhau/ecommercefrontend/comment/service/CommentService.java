package com.tma.vlhau.ecommercefrontend.comment.service;

import com.tma.vlhau.ecommercecommon.entity.Comment;
import com.tma.vlhau.ecommercecommon.entity.product.Product;
import com.tma.vlhau.ecommercefrontend.comment.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {


    public static final int COMMENT_PER_PAGE = 8;

    @Autowired
    private CommentRepository commentRepository;

    public Comment findById(Integer id){
        return commentRepository.findById(id).get();
    }


    public void save(Comment comment){
        commentRepository.save(comment);
    }

    public List<Comment> getCommentsByProductAndDateOrderByAsc(Product product){
        return commentRepository.getCommentsByProductAndDateOrderByAsc(product);
    }

    public Page<Comment> listByPage(int pageNum, Product product) {
        Sort sort = Sort.by("date");

        sort = sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, COMMENT_PER_PAGE, sort);

        return commentRepository.findAllBy(product,pageable);

    }


}
