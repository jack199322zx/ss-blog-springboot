package com.sstyle.server.service.impl;

import com.sstyle.server.context.SpringContextHolder;
import com.sstyle.server.context.event.NotifyEvent;
import com.sstyle.server.domain.Comment;
import com.sstyle.server.mapper.CommentMapper;
import com.sstyle.server.service.CommentService;
import com.sstyle.server.utils.ThreadContext;
import com.sstyle.server.web.constants.Constants;
import org.apache.commons.lang3.StringUtils;
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
    public Comment saveComment(Comment comment) {
        long id = Id.next();
        comment.setCommentId(String.valueOf(id));
        comment.setUserId(ThreadContext.getStaffId());
        NotifyEvent notifyEvent = new NotifyEvent("NotifyEvent");
        notifyEvent.setAssociateId(comment.getArticleId());
        notifyEvent.setFromUserId(comment.getUserId());
        if (StringUtils.isNotEmpty(comment.getToCommentId())) {
            //回复
            //去找评论
            Comment comm = findCommentById(comment.getToCommentId());
            notifyEvent.setToUserId(comm.getUserId());
            notifyEvent.setEventType(Constants.NOTIFY_EVENT_COMMENT_REPLY);
        }else {
            //评论
            notifyEvent.setEventType(Constants.NOTIFY_EVENT_COMMENT);
        }
        SpringContextHolder.publishEvent(notifyEvent);
        logger.info("comment=============={}",comment);
        if (commentMapper.saveComment(comment) == 1) {
            return comment;
        }
        return null;
    }

    @Override
    public Comment findCommentById(String commentId) {
        return commentMapper.findCommentById(commentId);
    }
}
