package com.sstyle.server.service.impl;

import com.sstyle.server.domain.Comment;
import com.sstyle.server.mapper.CommentMapper;
import com.sstyle.server.service.CommentService;
import com.sstyle.server.utils.ThreadContext;
import org.n3r.idworker.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author ss
 * @Date 2018/4/27 16:56
 */
@Service
public class CommentServiceImpl implements CommentService{
    @Autowired
    private CommentMapper commentMapper;

    private Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    @Override
    public List<Comment> queryCommentsByArticleId(String articleId) {
        List<Comment> commentList = commentMapper.queryCommentsByArticle(articleId);
        logger.info("commentList==========={}", commentList);
        return commentList;
    }

    @Override
    @Transactional
    public int saveComment(Comment comment, String commentId) {
        long id = Id.next();
        comment.setCommentId(String.valueOf(id));
        comment.setUserId(ThreadContext.getStaffId());
        commentMapper.updateCommentByReceiveId(String.valueOf(id), commentId);
        return  commentMapper.saveComment(comment);
    }
}
