package com.sstyle.server.service;


import com.sstyle.server.domain.Article;
import com.sstyle.server.domain.JSONResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by ss on 2018/4/5.
 */
public interface BlogService {
    Map<String, Object> initBlog(int page, int dist);
    Map<String, Object> initBlogList();
    JSONResult queryLoginInfo(HttpServletRequest request);
    Map<String, Object> queryArticleDetail(String articleId);
    int saveFavoriteByArticleId(String articleId, String userId);
    int cancelFavoriteByArticleId(String articleId, String userId);
}
