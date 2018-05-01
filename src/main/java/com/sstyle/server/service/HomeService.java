package com.sstyle.server.service;

import com.sstyle.server.domain.Article;
import com.sstyle.server.domain.Comment;

import java.util.List;
import java.util.Map;

/**
 * Created by ss on 2018/4/30.
 */
public interface HomeService {
    List<Map> queryDynamics(String userId);
    List<Article> queryMyArticlesById(String userId);
    List<Map> queryMyCommentsById(String userId);
    List<Map> queryMyNotify(String userId);
}
