package com.sstyle.server.mapper;

import com.sstyle.server.domain.Article;
import com.sstyle.server.domain.Comment;
import com.sstyle.server.domain.Feeds;
import com.sstyle.server.domain.Notify;

import java.util.List;

/**
 * Created by ss on 2018/5/1.
 */
public interface HomeMapper {
    List<Feeds> queryDynamicsById(String userId);
    List<Article> queryMyArticlesById(String userId);
    List<Comment> queryMyCommentsById(String userId);
    List<Notify> queryMyNotifyById(String userId);
}
