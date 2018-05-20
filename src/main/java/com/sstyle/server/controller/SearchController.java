package com.sstyle.server.controller;

import com.sstyle.server.domain.JSONResult;
import com.sstyle.server.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Future;

/**
 * Created by ss on 2018/5/5.
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @RequestMapping("/find-keywords")
    public JSONResult findKeywords(@RequestParam String keywords) {
        return new JSONResult(searchService.findKeywords(keywords));
    }

    @RequestMapping("/search-articles")
    public JSONResult searchArticles(@RequestParam String search) {
        return new JSONResult(searchService.searchArticle(search));
    }

    @RequestMapping("/save-keywords")
    public JSONResult saveKeywords(@RequestParam String keywords) {
        return new JSONResult(searchService.saveKeywords(keywords));
    }

    /**
     * 手动更新索引接口
     * @return
     */
    @RequestMapping("/generate-es")
    public void generateEs() {
        Future<String> future = searchService.deleteIndex();
        searchService.generateEsIndex(future);
    }

}
