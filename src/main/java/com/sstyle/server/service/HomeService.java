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
    Map<String, Object> queryDynamics(String userId, int page);
    Map<String, Object> queryMyArticlesById(String userId, int page);
    Map<String, Object> queryMyCommentsById(String userId, int page);
    Map<String, Object> queryMyNotify(String userId, int page);
    Map<String, Object> queryMyFavoritesById(String userId, int page);
    Map<String, Object> queryMyFollow(String userId, int page);
    Map<String, Object> queryMyFans(String userId, int page);
}
