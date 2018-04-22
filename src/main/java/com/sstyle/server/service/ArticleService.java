package com.sstyle.server.service;

import com.sstyle.server.domain.JSONResult;
import com.sstyle.server.domain.Mavon;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by ss on 2018/4/21.
 */
public interface ArticleService {
    int saveArticle(Mavon mavon);
    JSONResult backUrl(MultipartFile file) throws IOException;
}
