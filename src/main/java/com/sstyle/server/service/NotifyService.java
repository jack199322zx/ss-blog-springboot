package com.sstyle.server.service;

import com.sstyle.server.domain.Notify;

import java.util.Map;

/**
 * Created by ss on 2018/4/30.
 */
public interface NotifyService {


    /**
     * 发送通知
     * @param notify
     */
    void send(Notify notify);

    /**
     * 未读消息数量
     * @param userId
     * @return
     */
    int unread4Me(String userId);

    /**
     * 标记为已读
     * @param notify
     */
    int readed4Me(Notify notify);

    int deleteNotifyByArticle(String articleId);

    int deleteNotifyByCancelFavorite(String userId, String articleId);

    int deleteNotifyByCancelComment(String commentId);
}
