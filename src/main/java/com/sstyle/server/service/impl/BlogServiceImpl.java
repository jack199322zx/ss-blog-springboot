package com.sstyle.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sstyle.server.domain.Article;
import com.sstyle.server.domain.Flag;
import com.sstyle.server.domain.JSONResult;
import com.sstyle.server.domain.User;
import com.sstyle.server.mapper.BlogMapper;
import com.sstyle.server.service.BaseService;
import com.sstyle.server.service.BlogService;
import com.sstyle.server.utils.MapUtils;
import com.sstyle.server.utils.RedisClient;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ss on 2018/4/5.
 */
@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private BaseService baseService;

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private int pageSize = 6;

    private Logger logger = LoggerFactory.getLogger(BlogServiceImpl.class);

    public Map<String, Object> initBlog(int page, int dist) {
        List<Article> articleDistList = blogMapper.queryAllArticlesByDist(dist);
        int size = articleDistList.size();
        int pageCount = calculatePageCount(size, pageSize);
        int start = page * pageSize;
        int end = (page + 1) * pageSize;
        end = end > size ? size: end;
        return MapUtils.of("articleList", articleDistList.subList(start, end), "pageCount", pageCount);
    }

    @Override
    public Map<String, Object> initBlogList() {
        List<Article> articleList = baseService.queryAllArticles();
        List<Article> articleDistList = blogMapper.queryAllArticlesByDist(0);
        int size = articleDistList.size();
        int pageCount = calculatePageCount(size, pageSize);
        List<Flag> flagList = baseService.queryAllFlags();
        List<Article> viewNumSortedList = baseService.queryArticlesByViewNum(articleList, pageSize);
        List<Article> createTimeSortedList = baseService.queryArticlesByCreateTime(articleList, pageSize);
        List<Article> newCommentsSortedList = baseService.queryArticlesByNewComments(articleList, pageSize);
        return MapUtils.of("flagList", flagList,
                "viewNumSortedList", viewNumSortedList,
                "createTimeSortedList", createTimeSortedList,
                "newCommentsSortedList", newCommentsSortedList,
                "articleDistList", articleDistList,
                "pageCount", pageCount);

    }

    public JSONResult queryLoginInfo(HttpServletRequest request) {
        String Json = RedisClient.get(request.getHeader(AUTHORIZATION_HEADER));
        if (Json != null) {
            User user = JSONObject.parseObject(Json, User.class);
            User backUser = new User();
            backUser.setUserName(user.getUserName());
            backUser.setId(user.getId());
            return new JSONResult(backUser);
        }
        return new JSONResult("failed");
    }

    private int calculatePageCount(int size, int pageSize) {
        int pageCount;
        if (size < pageSize){
            pageCount = 1;
        } else if (size%pageSize > 0){
            pageCount = size/pageSize + 1;
        } else {
            pageCount = size/pageSize;
        }
        return pageCount;
    }

}
