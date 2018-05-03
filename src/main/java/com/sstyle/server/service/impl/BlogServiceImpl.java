package com.sstyle.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sstyle.server.context.SpringContextHolder;
import com.sstyle.server.context.event.NotifyEvent;
import com.sstyle.server.domain.*;
import com.sstyle.server.mapper.ArticleMapper;
import com.sstyle.server.mapper.BlogMapper;
import com.sstyle.server.mapper.UserMapper;
import com.sstyle.server.service.*;
import com.sstyle.server.utils.MapUtils;
import com.sstyle.server.utils.RedisClient;
import com.sstyle.server.web.cache.ArticleCache;
import com.sstyle.server.web.cache.FlagCache;
import com.sstyle.server.web.constants.Constants;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private CommentService commentService;
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private FeedsService feedsService;
    @Autowired
    private ArticleCache articleCache;
    @Autowired
    private FlagCache flagCache;

    private Logger logger = LoggerFactory.getLogger(BlogServiceImpl.class);

    public Map<String, Object> initBlog(int page, int dist) {
        List<Article> articleDistList = articleCache.queryPageArticlesByDist(dist);
        int size = articleDistList.size();
        logger.info("articleDistList============={}", articleDistList);
        int pageCount = calculatePageCount(size, Constants.PAGE_SIZE);
        int start = page * Constants.PAGE_SIZE;
        int end = (page + 1) * Constants.PAGE_SIZE;
        end = end > size ? size: end;
        return MapUtils.of("articleList", articleDistList.subList(start, end), "pageCount", pageCount);
    }

    @Override
    public Map<String, Object> initBlogList() {
        List<Article> articleList = articleCache.queryAllArticles();
        List<Article> articleDistList = articleCache.queryPageArticlesByDist(0);
        int size = articleDistList.size();
        int pageCount = calculatePageCount(size, Constants.PAGE_SIZE);
        List<Flag> flagList = flagCache.queryAllFlags();
        List<Article> viewNumSortedList = queryArticlesByViewNum(articleList, Constants.PAGE_SIZE);
        List<Article> createTimeSortedList = queryArticlesByCreateTime(articleList, Constants.PAGE_SIZE);
        List<Article> commentsSortedList = queryArticlesByComments(articleList, Constants.PAGE_SIZE);
        return MapUtils.of("flagList", flagList,
                "viewNumSortedList", viewNumSortedList,
                "createTimeSortedList", createTimeSortedList,
                "commentsSortedList", commentsSortedList,
                "articleDistList", articleDistList,
                "pageCount", pageCount);

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

    @Override
    @Transactional
    public Map<Object, Object> queryArticleDetail(String articleId) {
        Article article = blogMapper.queryArticleDetailById(articleId);
        int viewNum = article.getViewNum() + 1;
        blogMapper.updateViewNum(viewNum, articleId);
        article.setViewNum(viewNum);
        int publishNum = blogMapper.queryPublishArticleNum(article.getUser().getId());
        int commentsNum = blogMapper.queryCommentsNum(article.getUser().getId());
        List<Article> articleList = articleCache.queryAllArticles();
        List<Article> viewNumSortedList = queryArticlesByViewNum(articleList, Constants.PAGE_SIZE);
        List<Article> createTimeSortedList = queryArticlesByCreateTime(articleList, Constants.PAGE_SIZE);
        List<Article> commentsSortedList = queryArticlesByComments(articleList, Constants.PAGE_SIZE);
        List<Comment> commentsList = commentService.queryCommentsByArticleId(articleId);
        List<Comment> filterCommentsList = commentsList.stream().map(comm -> {
            List<Comment> list = commentsList.stream().filter(c -> c.getCommentId().equals(comm.getToCommentId())).collect(Collectors.toList());
            if (list !=null && list.size()> 0) {
                Comment coment = list.get(0);
                comm.setToComment(coment);
            }
            return comm;
        }).collect(Collectors.toList());
        return MapUtils.asMap("article", article,
                "publishNum", publishNum,
                "commentsNum", commentsNum,
                "viewNumSortedList", viewNumSortedList,
                "createTimeSortedList", createTimeSortedList,
                "commentsSortedList", commentsSortedList,
                "commentList", filterCommentsList
                );
    }


    @Override
    public JSONResult queryLoginInfo(HttpServletRequest request) {
        String token = request.getHeader(Constants.AUTHORIZATION_HEADER);
        String Json = RedisClient.get(token);
        if (Json != null) {
            RedisClient.expire(token, Constants.TOKEN_KEY_EXPIRE_TIME);
            User user = JSONObject.parseObject(Json, User.class);
            Map<String, Object> userMap = findUserDynamicsAndNotify(user.getId());
            user.setNotifyAndDynamics(userMap);
            return new JSONResult(user);
        }
        return new JSONResult("failed");
    }

    /**
     * 查询用户未读消息，动态数量
     * @Return
     * @Date 2018/5/2 14:36
     */
    private Map<String, Object> findUserDynamicsAndNotify(String userId) {
        int unreadNum = notifyService.unread4Me(userId);
        int dynamicNum = feedsService.unreadDynamics(userId);
        return MapUtils.of("unreadNum", unreadNum, "dynamicNum", dynamicNum);
    }

    private List<Article> queryArticlesByViewNum(List<Article> articleList, int pageSize) {
        List<Article> viewNumSortedList = articleList.stream()
                .sorted((article1, article2) -> article2.getViewNum() - article1.getViewNum())
                .limit(pageSize)
                .collect(Collectors.toList());
        return viewNumSortedList;
    }

    private List<Article> queryArticlesByCreateTime(List<Article> articleList, int pageSize) {
        List<Article> createTimeSortedList = articleList.stream()
                .sorted((article1, article2) -> article2.getCreateTime().compareTo(article1.getCreateTime()))
                .limit(pageSize)
                .collect(Collectors.toList());
        return  createTimeSortedList;
    }

    private List<Article> queryArticlesByComments(List<Article> articleList, int pageSize) {
        List<Article> commentsSortedList = articleList.stream()
                .sorted((article1, article2) -> article2.getCommentsNum() - article1.getCommentsNum())
                .limit(pageSize)
                .collect(Collectors.toList());
        return commentsSortedList;
    }
}
