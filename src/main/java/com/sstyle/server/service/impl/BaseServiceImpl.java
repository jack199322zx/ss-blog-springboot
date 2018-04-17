package com.sstyle.server.service.impl;

import com.sstyle.server.domain.Article;
import com.sstyle.server.domain.Flag;
import com.sstyle.server.mapper.BlogMapper;
import com.sstyle.server.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ss on 2018/4/15.
 */
@Service
@CacheConfig(cacheNames = "blogCaches")
public class BaseServiceImpl implements BaseService{

    private Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);

    @Autowired
    private BlogMapper blogMapper;

    @Cacheable(key="001")
    @Override
    public List<Article> queryAllArticles() {
        return blogMapper.queryArticles();
    }

    @Cacheable(key="002")
    @Override
    public List<Flag> queryAllFlags() {
        return blogMapper.queryTecFlags();
    }

    @Override
    public List<Article> queryArticlesByViewNum(List<Article> articleList, int pageSize) {
        List<Article> viewNumSortedList = articleList.stream()
                .sorted((article1, article2) -> article2.getViewNum() - article1.getViewNum())
                .limit(pageSize)
                .collect(Collectors.toList());
        return viewNumSortedList;
    }


    @Override
    public List<Article> queryArticlesByCreateTime(List<Article> articleList, int pageSize) {
        List<Article> createTimeSortedList = articleList.stream()
                .sorted((article1, article2) -> article2.getCreateTime().compareTo(article1.getCreateTime()))
                .limit(pageSize)
                .collect(Collectors.toList());
        return  createTimeSortedList;
    }

    @Override
    public List<Article> queryArticlesByNewComments(List<Article> articleList, int pageSize) {
        List<Article> newCommentsSortedList = articleList.stream()
                .sorted((article1, article2) -> article2.getUpdateTime().compareTo(article1.getUpdateTime()))
                .limit(pageSize)
                .collect(Collectors.toList());
        return newCommentsSortedList;
    }

}
