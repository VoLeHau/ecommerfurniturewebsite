package com.tma.vlhau.ecommercebackend.commentreply.service;

import com.tma.vlhau.ecommercebackend.commentreply.repository.CommentReplyRepository;
import com.tma.vlhau.ecommercecommon.entity.Comment;
import com.tma.vlhau.ecommercecommon.entity.CommentReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentReplyService {

    @Autowired
    private CommentReplyRepository commentReplyRepository;

    public void save(CommentReply commentReply, Comment comment){
        CommentReply reply = new CommentReply();
        reply.setText(commentReply.getText());
        reply.setDate(LocalDateTime.now());
        reply.setComment(comment);
        commentReplyRepository.save(reply);

    }

    public List<CommentReply> getCommentRepliesByComment(Comment comment){
        return commentReplyRepository.getCommentRepliesByComment(comment);
    }

    public List<CommentReply> findAll(){
        return commentReplyRepository.findAll();
    }

}
