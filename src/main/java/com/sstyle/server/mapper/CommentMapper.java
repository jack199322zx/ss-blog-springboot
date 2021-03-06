package com.sstyle.server.mapper;

import com.sstyle.server.domain.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author ss
 * @Date 2018/4/27 16:47
 */
public interface CommentMapper {
    List<Comment> queryCommentsByArticle(String articleId);
    int saveComment(@Param("comment") Comment comment);
    Comment findCommentById(String commentId);
    int deleteCommentById(String commentId);
}
