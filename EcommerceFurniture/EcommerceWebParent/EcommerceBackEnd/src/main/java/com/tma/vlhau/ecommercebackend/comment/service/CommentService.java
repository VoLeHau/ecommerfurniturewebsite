package com.tma.vlhau.ecommercebackend.comment.service;

import com.tma.vlhau.ecommercebackend.comment.repository.CommentRepository;
import com.tma.vlhau.ecommercecommon.entity.Brand;
import com.tma.vlhau.ecommercecommon.entity.Comment;
import com.tma.vlhau.ecommercecommon.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    public static final int COMMENT_PER_PAGE = 5;
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


    public List<Comment> listAll() {
        Sort sort = Sort.by("date").ascending();

        return (List<Comment>) commentRepository.findAll(sort);
    }

    public Page<Comment> listByPage(int pageNum, String sortDir, String keyword) {
        Sort sort = Sort.by("date");

        if (sortDir.equals("asc")) {
            sort = sort.ascending();
        } else {
            sort = sort.descending();
        }

        Pageable pageable = PageRequest.of(pageNum - 1, COMMENT_PER_PAGE, sort);

        if (keyword != null) {
            return commentRepository.findAllBy(keyword, pageable);
        } else {
            return commentRepository.findAllBy(pageable);
        }

    }
}
