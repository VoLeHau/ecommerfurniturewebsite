package com.tma.vlhau.ecommercebackend.comment.controller;


import com.tma.vlhau.ecommercebackend.comment.service.CommentService;
import com.tma.vlhau.ecommercebackend.commentreply.service.CommentReplyService;
import com.tma.vlhau.ecommercecommon.entity.Comment;
import com.tma.vlhau.ecommercecommon.entity.CommentReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CommentController {

    @Autowired private CommentService commentService;

    @Autowired private CommentReplyService commentReplyService;

    @GetMapping("/comments")
    public String listFirstPage(@Param("sortDir") String sortDir, Model model) {
        return listByPage(model, 1, sortDir, null);
    }

    @GetMapping("/comments/page/{pageNum}")
    private String listByPage(Model model, @PathVariable(name = "pageNum") int pageNum,
                              @Param("sortDir") String sortDir,
                              @Param("keyword") String keyword) {
        if (sortDir == null || sortDir.isEmpty()) {
            sortDir = "desc";
        }

        Page<Comment> listCommentsPage = commentService.listByPage(pageNum, sortDir, keyword);
        List<Comment> listComments = listCommentsPage.getContent();

        long startCount = (long) (pageNum - 1) * CommentService.COMMENT_PER_PAGE + 1;
        long endCount = startCount + CommentService.COMMENT_PER_PAGE - 1;

        if (endCount > listCommentsPage.getTotalElements()) {
            endCount = listCommentsPage.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("desc") ? "asc" : "desc";

        model.addAttribute("sortOrder", sortDir);
        model.addAttribute("reverseSortOrder", reverseSortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listComments", listComments);
        model.addAttribute("listCommentsReply", commentReplyService.findAll());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalItems", listCommentsPage.getTotalElements());
        model.addAttribute("totalPages", listCommentsPage.getTotalPages());
        model.addAttribute("sortField", "name");

        model.addAttribute("module", "comments");

        return "comment/comments";
    }

    @GetMapping("/comments/reply/{id}")
    public String replyComment(@PathVariable(name = "id") Integer id, Model model) {

        Comment comment = commentService.findById(id);

        model.addAttribute("comment", comment);
        model.addAttribute("commentReply", new CommentReply());
        model.addAttribute("pageTitle", "Reply Comment (ID: " + id + ")");
        model.addAttribute("listCommentsReply", commentReplyService.getCommentRepliesByComment(comment));
        return "comment/comment_form";

    }



}
