package com.sstyle.server.controller;


import com.alibaba.fastjson.JSON;
import com.sstyle.server.domain.JSONResult;
import com.sstyle.server.service.BlogService;
import org.apache.commons.collections.MapUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ss on 2018/3/24.
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public JSONResult initBlog(@RequestParam int page, @RequestParam int dist) {
        return new JSONResult(blogService.initBlog(page, dist));
    }

    @RequestMapping(value = "/blog-list", method = RequestMethod.POST)
    public JSONResult initBlogList() {
        return new JSONResult(blogService.initBlogList());
    }

    @RequestMapping(value = "/check-login", method = RequestMethod.POST)
    public JSONResult checkLogin(HttpServletRequest request) {
        return blogService.queryLoginInfo(request);
    }

    @RequestMapping(value = "/blog-detail", method = RequestMethod.POST)
    public JSONResult initBlogDetail(@RequestParam String articleId) {
        return new JSONResult(blogService.queryArticleDetail(articleId));
    }

}
