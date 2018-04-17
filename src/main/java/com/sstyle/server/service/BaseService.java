package com.sstyle.server.service;

import com.sstyle.server.domain.Article;
import com.sstyle.server.domain.Flag;

import java.util.List;
import java.util.Map;

/**
 * Created by ss on 2018/4/15.
 */
public interface BaseService {
    List<Article> queryAllArticles();
    List<Flag> queryAllFlags();
    List<Article> queryArticlesByViewNum(List<Article> articleList, int pageSize);
    List<Article> queryArticlesByCreateTime(List<Article> articleList, int pageSize);
    List<Article> queryArticlesByNewComments(List<Article> articleList, int pageSize);
}
