package com.sstyle.server.service;

import com.sstyle.server.domain.Comment;

import java.util.List;

/**
 * @Author ss
 * @Date 2018/4/27 16:56
 */
public interface CommentService {
    List<Comment> queryCommentsByArticleId(String articleId);
    int saveComment(Comment comment, String commentId);
}
