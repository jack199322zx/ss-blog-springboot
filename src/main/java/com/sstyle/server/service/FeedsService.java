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
     * 删除动态，取消关注时，删除之前此人的动态
     *
     * @param ownId
     * @param authorId
     * @return
     */
    int deleteByAuthorId(long ownId, long authorId);

    List<Feeds> findUserFeeds(String userId);

    /**
     * 删除文章时触发动态删除
     *
     * @param postId
     */
    void deleteByTarget(long postId);
}
