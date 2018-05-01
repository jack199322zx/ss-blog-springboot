package com.sstyle.server.service.impl;

import com.sstyle.server.domain.*;
import com.sstyle.server.mapper.BlogMapper;
import com.sstyle.server.mapper.HomeMapper;
import com.sstyle.server.mapper.UserMapper;
import com.sstyle.server.service.HomeService;
import com.sstyle.server.utils.MapUtils;
import org.apache.commons.lang3.StringUtils;
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

    public List<Map> queryDynamics(String userId) {
        List<Feeds> feedsList = homeMapper.queryDynamicsById(userId);
        List<Map> dynamicList = new ArrayList<>();
        if (feedsList !=null && feedsList.size()> 0) {
            feedsList.stream().forEach(feed -> {
                Article article = blogMapper.queryArticleDetailById(feed.getAssociationId());
                Map dataMap = new HashMap();
                dataMap.put("userAvatar", article.getUser().getAvatar());
                dataMap.put("userCode", article.getUser().getUserCode());
                dataMap.put("article", article);
                dynamicList.add(dataMap);
            });
        }
        return dynamicList;
    }

    @Override
    public List<Article> queryMyArticlesById(String userId) {
        return homeMapper.queryMyArticlesById(userId);
    }

    @Override
    public List<Map> queryMyCommentsById(String userId) {
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
    public List<Map> queryMyNotify(String userId) {
        List<Notify> notifyList = homeMapper.queryMyNotifyById(userId);
        List<Map> myNotifyList = new ArrayList<>();
        if (notifyList !=null && notifyList.size()> 0) {
            notifyList.stream().forEach(ntf -> {
                User user = userMapper.queryUserInfo(ntf.getFromId());
                Article article = new Article();
                if (StringUtils.isNotEmpty(ntf.getAssociationId())) {
                   article = blogMapper.queryArticleDetailById(ntf.getAssociationId());
                }
                myNotifyList.add(MapUtils.of("article", article, "user", user));
            });
        }
        return myNotifyList;
    }
}
