package com.sstyle.server.controller;

import com.sstyle.server.domain.JSONResult;
import com.sstyle.server.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ss on 2018/4/30.
 */
@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @RequestMapping(value = "/query-dynamics", method = RequestMethod.POST)
    public JSONResult queryDynamics(@RequestParam String userId) {
        return new JSONResult(homeService.queryDynamics(userId));
    }

    @RequestMapping(value = "/query-my-articles", method = RequestMethod.POST)
    public JSONResult queryMyArticles(@RequestParam String userId) {
        return new JSONResult(homeService.queryMyArticlesById(userId));
    }

    @RequestMapping(value = "/query-my-comments", method = RequestMethod.POST)
    public JSONResult queryMyComments(@RequestParam String userId) {
        return new JSONResult(homeService.queryMyCommentsById(userId));
    }

    @RequestMapping(value = "/query-my-notify", method = RequestMethod.POST)
    public JSONResult queryMyNotify(@RequestParam String userId) {
        return new JSONResult(homeService.queryMyNotify(userId));
    }
}
