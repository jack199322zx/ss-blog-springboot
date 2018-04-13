package com.sstyle.server.service;


import java.util.Map;

/**
 * Created by ss on 2018/4/5.
 */
public interface BlogService {
    Map<String, Object> initBlog(int page);
    Map<String, Object> initBlogList(Map params);
}
