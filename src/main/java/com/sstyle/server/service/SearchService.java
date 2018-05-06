package com.sstyle.server.service;

import com.sstyle.server.domain.Article;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by ss on 2018/5/5.
 */
public interface SearchService {
    List<String> findKeywords(String keywords);
    List<Article> saveKeywords(String keywords);
    void generateEsIndex(Future<String> future);
    Future<String> deleteIndex();
}
