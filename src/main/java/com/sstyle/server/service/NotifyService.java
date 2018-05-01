package com.sstyle.server.service;

import com.sstyle.server.domain.Notify;

/**
 * Created by ss on 2018/4/30.
 */
public interface NotifyService {

    Notify findByToId(String toId);
    /**
     * 发送通知
     * @param notify
     */
    void send(Notify notify);

    /**
     * 未读消息数量
     * @param ownId
     * @return
     */
    int unread4Me(long ownId);

    /**
     * 标记为已读
     * @param ownId
     */
    void readed4Me(long ownId);

}
