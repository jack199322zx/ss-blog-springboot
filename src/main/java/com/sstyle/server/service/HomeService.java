package com.sstyle.server.service;

import com.sstyle.server.domain.Article;
import com.sstyle.server.domain.Comment;
import com.sstyle.server.domain.User;

import java.util.List;
import java.util.Map;

/**
 * Created by ss on 2018/4/30.
 */
public interface HomeService {
    List<Map> queryDynamics(String userId, int page);
    List<Article> queryMyArticlesById(String userId, int page);
    List<Map> queryMyCommentsById(String userId, int page);
    List<Map> queryMyNotify(String userId, int page);
    List<Article> queryMyFavoritesById(String userId, int page);
    List<User> queryMyFollow(String userId);
    List<User> queryMyFans(String userId);
}
