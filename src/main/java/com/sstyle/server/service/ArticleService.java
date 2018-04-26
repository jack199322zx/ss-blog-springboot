package com.sstyle.server.service;

import com.sstyle.server.domain.Flag;
import com.sstyle.server.domain.JSONResult;
import com.sstyle.server.domain.Mavon;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by ss on 2018/4/21.
 */
public interface ArticleService {
    int saveArticle(Mavon mavon);
    JSONResult backUrl(MultipartFile file) throws IOException;
    JSONResult delImg(String filename);
    Map<String, Object> queryFlagByDist(int dist);
}
