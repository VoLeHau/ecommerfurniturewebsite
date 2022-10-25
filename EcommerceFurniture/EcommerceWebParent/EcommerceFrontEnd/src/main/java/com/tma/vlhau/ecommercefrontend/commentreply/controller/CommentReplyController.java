package com.tma.vlhau.ecommercefrontend.commentreply.controller;

import com.tma.vlhau.ecommercecommon.entity.Comment;
import com.tma.vlhau.ecommercecommon.entity.CommentReply;
import com.tma.vlhau.ecommercefrontend.comment.service.CommentService;
import com.tma.vlhau.ecommercefrontend.commentreply.service.CommentReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class CommentReplyController {

    @Autowired
    private CommentService commentService;

    @Autowired private CommentReplyService commentReplyService;

    @PostMapping("/comments/reply/save")
    public String saveBrand(CommentReply commentReply, @RequestParam(value = "id") Integer id, Model model){
        Comment comment = commentService.findById(id);
        commentReplyService.save(commentReply, comment);
        return "redirect:/comments";
    }
}
