package com.tma.vlhau.ecommercefrontend.commentreply.repository;

import com.tma.vlhau.ecommercecommon.entity.Comment;
import com.tma.vlhau.ecommercecommon.entity.CommentReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentReplyRepository extends JpaRepository<CommentReply, Integer> {


    @Query("select cm from CommentReply cm where cm.comment = ?1")
    List<CommentReply> getCommentRepliesByComment(Comment comment);

}
