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

    public List<Map> queryDynamics(String userId, int page) {
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
        return dynamicList;
    }

    @Override
    public Map<String, Object> queryMyArticlesById(String userId, int page) {
        PageHelper.startPage(page, Constants.HOME_PAGE_SIZE);
        List<Article> articleList = homeMapper.queryMyArticlesById(userId);
        PageInfo<Article> pageInfo = new PageInfo<>(articleList);
        return MapUtils.of("articleList", articleList, "pageCount", pageInfo.getPages());
    }

    @Override
    public List<Map> queryMyCommentsById(String userId, int page) {
        List<Comment> commentList = homeMapper.queryMyCommentsById(userId);
        List<Map> myCommentsList = new ArrayList<>();
        if (commentList !=null && commentList.size()> 0) {
            commentList.stream().forEach(comm -> {
                Article article = blogMapper.queryArticleDetailById(comm.getArticleId());
                myCommentsList.add(MapUtils.of("article", article, "comment", comm));
            });
        }
        return myCommentsList;
    }

    @Override
    public List<Map> queryMyNotify(String userId, int page) {
        List<Notify> notifyList = homeMapper.queryMyNotifyById(userId);
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
        return myNotifyList;
    }

    @Override
    public List<Article> queryMyFavoritesById(String userId, int page) {
        return homeMapper.queryMyFavoritesById(userId);
    }

    @Override
    public List<User> queryMyFollow(String userId) {
        return homeMapper.queryMyFollow(userId);
    }

    @Override
    public List<User> queryMyFans(String userId) {
        return homeMapper.queryMyFans(userId);
    }
}
