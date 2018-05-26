package com.sstyle.server.service.impl;

import com.sstyle.server.context.SpringContextHolder;
import com.sstyle.server.context.event.FeedsEvent;
import com.sstyle.server.context.event.NotifyEvent;
import com.sstyle.server.domain.*;
import com.sstyle.server.mapper.ArticleMapper;
import com.sstyle.server.mapper.BlogMapper;
import com.sstyle.server.service.ArticleService;
import com.sstyle.server.service.FeedsService;
import com.sstyle.server.service.NotifyService;
import com.sstyle.server.service.SearchService;
import com.sstyle.server.utils.*;
import com.sstyle.server.web.constants.Constants;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;

/**
 * Created by ss on 2018/4/21.
 */
@Service
@CacheConfig(cacheNames = "articleCaches")
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private FeedsService feedsService;
    @Autowired
    private NotifyService notifyService;
    @Value("${web.path}")
    private String location;

    private Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    @Override
    @Transactional
    @CacheEvict(allEntries = true) //发布文章需清除缓存
    public int saveArticle(Mavon mavon) {
        String markdown2Html = MarkdownUtil.ofContent(mavon.getMarkdown()).toString();
        String backImg = QiniuUtil.uploadBase64(mavon.getArticleImg());
        Article article = new Article();
        article.setArticleTitle(mavon.getTitle());
        article.setArticleImg(backImg);
        article.setArticleDesc(markdown2Html);
        article.setArticleSign(Optional.ofNullable(mavon.getSign()).orElse(""));
        String articleId;
        String mavonArticleId = mavon.getArticleId();
        if (StringUtils.isNotEmpty(mavonArticleId)) {
            //编辑文章
            articleId = mavonArticleId;
        }else {
            articleId = String.valueOf(Id.next());
        }
        article.setArticleId(articleId);
        article.setFlagList(mavon.getFlagList());
        article.setArticleType(mavon.getChannelId());
        mavon.getFlagList().stream().forEach(flag -> articleMapper.saveFlagByArticle(articleId, flag.getFlagId()));
        publishEvent(articleId, mavon.getUser());
        int row = articleMapper.saveArticle(article, mavon.getUser());
        return row;
    }

    @Override
    public JSONResult backUrl(MultipartFile file) throws IOException {
        if (file.isEmpty() || StringUtils.isBlank(file.getOriginalFilename())) {
            return new JSONResult("文件上传失败!");
        }
        String contentType = file.getContentType();
        if (!contentType.contains("")) {
            return new JSONResult("文件格式不正确!");
        }
        String root_fileName = file.getOriginalFilename();
        logger.info("上传图片:name={},type={}", root_fileName, contentType);
        //获取路径
        String return_path = ImageUtil.getFilePath(ThreadContext.getStaffId());
        String filePath = location + File.separator + return_path;
        logger.info("图片保存路径={}", filePath);
        String file_name = ImageUtil.saveImg(file, filePath);
        logger.info("返回文件名={}", file_name);
        return new JSONResult(Constants.IMG_PREFIX + return_path + File.separator + file_name);

    }
    @Override
    public JSONResult delImg(String filename) {
        String delPath = location + File.separator + ImageUtil.getFilePath(ThreadContext.getStaffId()) + File.separator + filename;
        return  ImageUtil.delImgFile(delPath)? new JSONResult("ok"): new JSONResult("failed");
    }

    @Override
    public Map<String, Object> queryFlagByDist(int dist) {
        return MapUtils.of("flagList", articleMapper.queryFlagByDist(dist));
    }

    /**
     * 发布事件
     * @return
     */
    private void publishEvent(String articleId, User user) {
        FeedsEvent feedsEvent = new FeedsEvent("FeedsEvent");
        feedsEvent.setAssociateId(articleId);
        feedsEvent.setFromUserId(user.getId());
        SpringContextHolder.publishEvent(feedsEvent);
    }

    @Override
    public Article queryEditArticleById(String articleId) {
        return blogMapper.queryArticleDetailById(articleId);
    }

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public int deleteArticle(String articleId) {
        // 删除文章相关的通知和动态，同时清除所有文章缓存
        feedsService.deleteByTarget(articleId);
        notifyService.deleteNotifyByArticle(articleId);
        int row = articleMapper.deleteArticleById(articleId);
        return row;
    }

    @Cacheable
    public List<Article> queryAllArticles() {
        return blogMapper.queryArticles();
    }

    @Cacheable(key="'article_' + #dist")
    public List<Article> queryPageArticlesByDist(int dist){
        return blogMapper.queryPageArticlesByDist(dist);
    }

}
