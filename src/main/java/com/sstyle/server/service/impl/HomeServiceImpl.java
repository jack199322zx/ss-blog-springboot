package com.sstyle.server.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sstyle.server.domain.*;
import com.sstyle.server.mapper.BlogMapper;
import com.sstyle.server.mapper.HomeMapper;
import com.sstyle.server.mapper.UserMapper;
import com.sstyle.server.service.BlogService;
import com.sstyle.server.service.HomeService;
import com.sstyle.server.utils.MapUtils;
import com.sstyle.server.web.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ss on 2018/4/30.
 */
@Service
public class HomeServiceImpl implements HomeService{
    @Autowired
    private HomeMapper homeMapper;
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private UserMapper userMapper;

    private Logger logger = LoggerFactory.getLogger(HomeServiceImpl.class);

    public Map<String, Object> queryDynamics(String userId, int page) {
        PageHelper.startPage(page, Constants.HOME_PAGE_SIZE);
        List<Feeds> feedsList = homeMapper.queryDynamicsById(userId);
        PageInfo<Feeds> pageInfo = new PageInfo<>(feedsList);
        logger.info("feedsList============{}", feedsList);
        List<Map> dynamicList = new ArrayList<>();
        if (feedsList !=null && feedsList.size()> 0) {
            feedsList.stream().forEach(feed -> {
                Article article = blogMapper.queryArticleDetailById(feed.getAssociationId());
                Map dataMap = new HashMap();
                dataMap.put("userAvatar", article.getUser().getAvatar());
                dataMap.put("userName", article.getUser().getUserName());
                dataMap.put("article", article);
                dataMap.put("feed", feed);
                dataMap.put("pageCount", pageInfo.getTotal());
                dynamicList.add(dataMap);
            });
        }
        return MapUtils.of("dynamicList", dynamicList, "pageCount", pageInfo.getPages());
    }

    @Override
    public Map<String, Object> queryMyArticlesById(String userId, int page) {
        PageHelper.startPage(page, Constants.HOME_PAGE_SIZE);
        List<Article> articleList = homeMapper.queryMyArticlesById(userId);
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        return MapUtils.of("articleList", articleList, "pageCount", pageInfo.getPages());
    }

    @Override
    public Map<String, Object> queryMyCommentsById(String userId, int page) {
        PageHelper.startPage(page, Constants.HOME_PAGE_SIZE);
        List<Comment> commentList = homeMapper.queryMyCommentsById(userId);
        PageInfo<Comment> pageInfo = new PageInfo<>(commentList);
        List<Map> myCommentsList = new ArrayList<>();
        if (commentList !=null && commentList.size()> 0) {
            commentList.stream().forEach(comm -> {
                Article article = blogMapper.queryArticleDetailById(comm.getArticleId());
                myCommentsList.add(MapUtils.of("article", article, "comment", comm));
            });
        }
        return MapUtils.of("myCommentsList", myCommentsList, "pageCount", pageInfo.getPages());
    }

    @Override
    public Map<String, Object> queryMyNotify(String userId, int page) {
        PageHelper.startPage(page, Constants.HOME_PAGE_SIZE);
        List<Notify> notifyList = homeMapper.queryMyNotifyById(userId);
        PageInfo<Notify> pageInfo = new PageInfo<>(notifyList);
        List<Map> myNotifyList = new ArrayList<>();
        if (notifyList !=null && notifyList.size()> 0) {
            notifyList.stream().forEach(ntf -> {
                User user = userMapper.queryUserInfo(ntf.getFromId());
                Article article = new Article();
                if (StringUtils.isNotEmpty(ntf.getAssociationId())) {
                   article = blogMapper.queryArticleDetailById(ntf.getAssociationId());
                }
                myNotifyList.add(MapUtils.of("article", article, "user", user, "notify", ntf));
            });
        }
        return MapUtils.of("myNotifyList", myNotifyList, "pageCount", pageInfo.getPages());
    }

    @Override
    public Map<String, Object> queryMyFavoritesById(String userId, int page) {
        PageHelper.startPage(page, Constants.HOME_PAGE_SIZE);
        List<Article> favoriteList = homeMapper.queryMyFavoritesById(userId);
        PageInfo<Article> pageInfo = new PageInfo<>(favoriteList);
        return MapUtils.of("favoriteList", favoriteList, "pageCount", pageInfo.getPages());
    }

    @Override
    public Map<String, Object> queryMyFollow(String userId, int page) {
        PageHelper.startPage(page, Constants.HOME_PAGE_SIZE);
        List<User> followList = homeMapper.queryMyFollow(userId);
        PageInfo<User> pageInfo = new PageInfo<>(followList);
        return MapUtils.of("followList", followList, "pageCount", pageInfo.getPages());
    }

    @Override
    public Map<String, Object> queryMyFans(String userId, int page) {
        PageHelper.startPage(page, Constants.HOME_PAGE_SIZE);
        List<User> fansList = homeMapper.queryMyFans(userId);
        PageInfo<User> pageInfo = new PageInfo<>(fansList);
        return MapUtils.of("fansList", fansList, "pageCount", pageInfo.getPages());
    }
}
