package com.sstyle.server.controller;

import com.sstyle.server.domain.JSONResult;
import com.sstyle.server.domain.Mavon;
import com.sstyle.server.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Created by ss on 2018/4/21.
 */
@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public JSONResult submitMavon(@RequestBody Mavon mavon) {
        return new JSONResult(articleService.saveArticle(mavon));
    }

    @RequestMapping(value = "/img-upload", method = RequestMethod.POST)
    public JSONResult uploadImg(@RequestParam("image") MultipartFile file) throws IOException {
        return articleService.backUrl(file);
    }

    @RequestMapping(value = "/img-delete", method = RequestMethod.POST)
    public JSONResult uploadImg(@RequestParam String file){
        return articleService.delImg(file);
    }

    @RequestMapping(value = "/queryFlagByDist", method = RequestMethod.POST)
    public JSONResult queryArticleFlagByDist(@RequestParam int dist){
        return new JSONResult(articleService.queryFlagByDist(dist));
    }

    @RequestMapping(value = "/edit-article", method = RequestMethod.POST)
    public JSONResult editArticle(@RequestParam String articleId){
        return new JSONResult(articleService.queryEditArticleById(articleId));
    }

    @RequestMapping(value = "/delete-article", method = RequestMethod.POST)
    public JSONResult deleteArticle(@RequestParam String articleId){
        return new JSONResult(articleService.deleteArticle(articleId));
    }
}
