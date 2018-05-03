package com.sstyle.server.service.impl;

import com.sstyle.server.domain.Notify;
import com.sstyle.server.mapper.NotifyMapper;
import com.sstyle.server.service.CommentService;
import com.sstyle.server.service.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by ss on 2018/4/30.
 */
@Service
public class NotifyServiceImpl implements NotifyService{

    @Autowired
    private NotifyMapper notifyMapper;

    @Override
    public void send(Notify notify) {
        if (notify == null) {
            return;
        }
        notifyMapper.saveNotify(notify);
    }

    @Override
    public int unread4Me(String userId) {
        return notifyMapper.findUnreadNotify(userId);
    }

    @Override
    public int readed4Me(Notify notify) {
       return notifyMapper.updateReadNotify(notify);
    }

    @Override
    public int deleteNotifyByArticle(String articleId) {
        return notifyMapper.deleteByArticleId(articleId);
    }

    @Override
    public int deleteNotifyByCancelFavorite(String userId, String articleId) {
        return notifyMapper.deleteNotifyByCancelFavorite(userId, articleId);
    }

    @Override
    public int deleteNotifyByCancelComment(String commentId) {
        return notifyMapper.deleteNotifyByCancelComment(commentId);
    }
}
