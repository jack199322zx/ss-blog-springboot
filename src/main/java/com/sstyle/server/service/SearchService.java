package com.sstyle.server.service;

import com.sstyle.server.domain.Article;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by ss on 2018/5/5.
 */
public interface SearchService {
    List<String> findKeywords(String keywords);
    int saveKeywords(String keywords);
    void generateEsIndex(Future<String> future);
    Future<String> deleteIndex();
    Map<String, Object> searchArticle(String keywords);
}
