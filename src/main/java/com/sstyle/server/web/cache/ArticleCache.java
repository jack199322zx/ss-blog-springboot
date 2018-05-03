package com.sstyle.server.web.cache;

import com.sstyle.server.domain.Article;
import com.sstyle.server.mapper.BlogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author ss
 * @Date 2018/5/3 17:43
 */
@Component
@CacheConfig(cacheNames = "articleCaches")
public class ArticleCache {
    private Logger logger = LoggerFactory.getLogger(ArticleCache.class);

    @Autowired
    private BlogMapper blogMapper;

    @Cacheable
    public List<Article> queryAllArticles() {
        return blogMapper.queryArticles();
    }

    @Cacheable(key="'article_' + #dist")
    public List<Article> queryPageArticlesByDist(int dist){
        return blogMapper.queryPageArticlesByDist(dist);
    }
}
