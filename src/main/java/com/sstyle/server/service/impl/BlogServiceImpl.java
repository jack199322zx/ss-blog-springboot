package com.sstyle.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.sstyle.server.domain.*;
import com.sstyle.server.mapper.BlogMapper;
import com.sstyle.server.service.*;
import com.sstyle.server.utils.MapUtils;
import com.sstyle.server.utils.RedisClient;
import com.sstyle.server.web.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.servlet.http.HttpServletRequest;
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
    private ArticleService articleService;
    @Autowired
    private FlagService flagService;

    private Logger logger = LoggerFactory.getLogger(BlogServiceImpl.class);

    public Map<String, Object> initBlog(int page, int dist) {
        List<Article> articleDistList = articleService.queryPageArticlesByDist(dist);
        int size = articleDistList.size();
        logger.info("articleDistList============={}", articleDistList);
        int pageCount = calculatePageCount(size, Constants.ARTICLE_PAGE_SIZE);
        int start = page * Constants.ARTICLE_PAGE_SIZE;
        int end = (page + 1) * Constants.ARTICLE_PAGE_SIZE;
        end = end > size ? size: end;
        return MapUtils.of("articleList", articleDistList.subList(start, end), "pageCount", pageCount);
    }

    @Override
    public Map<Object, Object> initBlogList() {
        List<Article> articleList = articleService.queryAllArticles();
        List<Article> articleDistList = articleService.queryPageArticlesByDist(0);
        int size = articleDistList.size();
        int pageCount = calculatePageCount(size, Constants.ARTICLE_PAGE_SIZE);
        List<Flag> flagList = flagService.queryAllFlags();
        List<Article> viewNumSortedList = queryArticlesByViewNum(articleList, Constants.ARTICLE_PAGE_SIZE);
        List<Article> createTimeSortedList = queryArticlesByCreateTime(articleList, Constants.ARTICLE_PAGE_SIZE);
        List<Article> commentsSortedList = queryArticlesByComments(articleList, Constants.ARTICLE_PAGE_SIZE);
        List<Map> userList = blogMapper.queryHotUser();
        return MapUtils.asMap("flagList", flagList,
                "viewNumSortedList", viewNumSortedList,
                "createTimeSortedList", createTimeSortedList,
                "commentsSortedList", commentsSortedList,
                "articleDistList", articleDistList,
                "pageCount", pageCount,
                "hotUserList", userList
                );

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
        List<Article> articleList = articleService.queryAllArticles();
        List<Article> viewNumSortedList = queryArticlesByViewNum(articleList, Constants.ARTICLE_PAGE_SIZE);
        List<Article> createTimeSortedList = queryArticlesByCreateTime(articleList, Constants.ARTICLE_PAGE_SIZE);
        List<Article> commentsSortedList = queryArticlesByComments(articleList, Constants.ARTICLE_PAGE_SIZE );
        List<Comment> commentsList = commentService.queryCommentsByArticleId(articleId);
        List<Comment> filterCommentsList = commentsList.stream().map(comm -> {
            List<Comment> list = commentsList.stream().filter(c -> c.getCommentId().equals(comm.getToCommentId())).collect(Collectors.toList());
            if (list !=null && list.size()> 0) {
                Comment coment = list.get(0);
                comm.setToComment(coment);
            }
            return comm;
        }).collect(Collectors.toList());
        List<Map> userList = blogMapper.queryHotUser();
        return MapUtils.asMap("article", article,
                "publishNum", publishNum,
                "commentsNum", commentsNum,
                "viewNumSortedList", viewNumSortedList,
                "createTimeSortedList", createTimeSortedList,
                "commentsSortedList", commentsSortedList,
                "commentList", filterCommentsList,
                "hotUserList", userList
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
