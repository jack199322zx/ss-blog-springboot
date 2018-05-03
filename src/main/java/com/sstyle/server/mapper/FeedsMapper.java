package com.sstyle.server.mapper;

import com.sstyle.server.domain.Feeds;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ss on 2018/5/1.
 */
public interface FeedsMapper {
    int saveFeeds(List<Feeds> feedsList);
    int unreadDynamics(String userId);
    int updateReadFeeds(Feeds feeds);
    int deleteFeedsByAuthor(@Param("userId") String userId, @Param("authorId") String authorId);
    int deleteByArticleId(String articleId);
}
