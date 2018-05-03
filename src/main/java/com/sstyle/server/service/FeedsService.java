package com.sstyle.server.service;

import com.sstyle.server.domain.Feeds;

import java.util.List;

/**
 * Created by ss on 2018/4/30.
 */
public interface FeedsService {
    /**
     * 添加动态, 同时会分发给粉丝
     *
     * @param feeds
     * @return
     */
    int add(Feeds feeds);

    /**
     * 取消关注时，删除之前此人的动态
     *
     * @param userId
     * @param authorId
     * @return
     */
    int deleteByAuthorId(String userId, String authorId);

    List<Feeds> findUserFeeds(String userId);

    /**
     * 删除文章时触发动态删除
     *
     * @param articleId
     */
    void deleteByTarget(String articleId);

    /**
     * 未读动态数量
     * @Param userId
     * @Return int
     * @Date 2018/5/2 14:55
     */
    int unreadDynamics(String userId);

    int readDynamics(Feeds feeds);
}
