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
    public JSONResult initBlog(@RequestParam int page) {
        return new JSONResult(blogService.initBlog(page));
    }

    @RequestMapping(value = "/blog-list", method = RequestMethod.POST)
    public JSONResult initBlogList(@RequestBody Map params) {
        return new JSONResult(blogService.initBlogList(params));
    }


}
