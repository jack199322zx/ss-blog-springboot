package com.sstyle.server.service.impl;

import com.sstyle.server.domain.Article;
import com.sstyle.server.domain.JSONResult;
import com.sstyle.server.domain.Mavon;
import com.sstyle.server.mapper.ArticleMapper;
import com.sstyle.server.service.ArticleService;
import com.sstyle.server.utils.AFAdminContext;
import com.sstyle.server.utils.ImageUtil;
import com.sstyle.server.utils.MarkdownUtil;
import com.sstyle.server.utils.ThreadContext;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by ss on 2018/4/21.
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Value("${web.upload-path}")
    private String location;

    private Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);


    @Override
    public int saveArticle(Mavon mavon) {
        String markdown2Html = MarkdownUtil.ofContent(mavon.getMarkdown()).toString();
        Article article = new Article();
        article.setArticleTitle(mavon.getTitle());
        article.setArticleDesc(markdown2Html);
        article.setArticleSign(Optional.ofNullable(mavon.getSign()).orElse(""));
        article.setArticleId(Id.next());
        article.setFlagList(mavon.getFlagList());
        article.setArticleType(mavon.getChannelId());
        return articleMapper.saveArticle(article, mavon.getUser());
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
        String filePath = location + return_path;
        logger.info("图片保存路径={}", filePath);
        String file_name = ImageUtil.saveImg(file, filePath);
        logger.info("返回文件名={}", file_name);
        return new JSONResult(return_path + File.separator + file_name);

    }
}
