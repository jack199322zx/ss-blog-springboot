package com.sstyle.server.mapper;

import com.sstyle.server.domain.Notify;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ss on 2018/4/30.
 */
public interface NotifyMapper {
    int saveNotify(Notify notify);
    int findUnreadNotify(String userId);
    int updateReadNotify(Notify notify);
    int deleteByArticleId(String articleId);
    int deleteNotifyByCancelFavorite(@Param("userId") String userId, @Param("articleId") String articleId);
    int deleteNotifyByCancelComment(String commentId);
}
