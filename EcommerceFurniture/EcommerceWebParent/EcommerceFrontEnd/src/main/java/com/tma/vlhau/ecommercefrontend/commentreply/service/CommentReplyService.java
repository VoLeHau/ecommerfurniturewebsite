package com.tma.vlhau.ecommercefrontend.commentreply.service;

import com.tma.vlhau.ecommercecommon.entity.Brand;
import com.tma.vlhau.ecommercecommon.entity.Comment;
import com.tma.vlhau.ecommercecommon.entity.CommentReply;
import com.tma.vlhau.ecommercefrontend.commentreply.repository.CommentReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentReplyService {

    @Autowired
    private CommentReplyRepository commentReplyRepository;

    public void save(CommentReply commentReply, Comment comment){
        commentReply.setDate(LocalDateTime.now());
        commentReply.setComment(comment);
        commentReplyRepository.save(commentReply);
    }

    public List<CommentReply> getCommentRepliesByComment(Comment comment){
        return commentReplyRepository.getCommentRepliesByComment(comment);
    }

    public List<CommentReply> findAll(){
        return commentReplyRepository.findAll();
    }



}
