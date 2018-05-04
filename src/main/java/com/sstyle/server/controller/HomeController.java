package com.sstyle.server.controller;

import com.sstyle.server.domain.Feeds;
import com.sstyle.server.domain.JSONResult;
import com.sstyle.server.domain.Notify;
import com.sstyle.server.service.BlogService;
import com.sstyle.server.service.FeedsService;
import com.sstyle.server.service.HomeService;
import com.sstyle.server.service.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by ss on 2018/4/30.
 */
@RestController
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private FeedsService feedsService;

    @RequestMapping(value = "/query-dynamics", method = RequestMethod.POST)
    public JSONResult queryDynamics(@RequestParam String userId, @RequestParam int page) {
        return new JSONResult(homeService.queryDynamics(userId, page));
    }

    @RequestMapping(value = "/query-my-articles", method = RequestMethod.POST)
    public JSONResult queryMyArticles(@RequestParam String userId, @RequestParam int page) {
        return new JSONResult(homeService.queryMyArticlesById(userId, page));
    }

    @RequestMapping(value = "/query-other-articles", method = RequestMethod.POST)
    public JSONResult queryOtherArticles(@RequestParam String userId, @RequestParam int page) {
        return new JSONResult(homeService.queryMyArticlesById(userId, page));
    }

    @RequestMapping(value = "/query-my-comments", method = RequestMethod.POST)
    public JSONResult queryMyComments(@RequestParam String userId, @RequestParam int page) {
        return new JSONResult(homeService.queryMyCommentsById(userId, page));
    }

    @RequestMapping(value = "/query-my-notify", method = RequestMethod.POST)
    public JSONResult queryMyNotify(@RequestParam String userId, @RequestParam int page) {
        return new JSONResult(homeService.queryMyNotify(userId, page));
    }

    @RequestMapping(value = "/query-my-favorites", method = RequestMethod.POST)
    public JSONResult queryMyFavorites(@RequestParam String userId, @RequestParam int page) {
        return new JSONResult(homeService.queryMyFavoritesById(userId, page));
    }

    @RequestMapping(value = "/query-other-favorites", method = RequestMethod.POST)
    public JSONResult queryOtherFavorites(@RequestParam String userId, @RequestParam int page) {
        return new JSONResult(homeService.queryMyFavoritesById(userId, page));
    }

    @RequestMapping(value = "/query-my-follow", method = RequestMethod.POST)
    public JSONResult queryMyFollow(@RequestParam String userId, @RequestParam int page) {
        return new JSONResult(homeService.queryMyFollow(userId, page));
    }

    @RequestMapping(value = "/query-other-follow", method = RequestMethod.POST)
    public JSONResult queryOtherFollow(@RequestParam String userId, @RequestParam int page) {
        return new JSONResult(homeService.queryMyFollow(userId, page));
    }

    @RequestMapping(value = "/query-my-fans", method = RequestMethod.POST)
    public JSONResult queryMyFans(@RequestParam String userId, @RequestParam int page) {
        return new JSONResult(homeService.queryMyFans(userId, page));
    }

    @RequestMapping(value = "/query-other-fans", method = RequestMethod.POST)
    public JSONResult queryOtherFans(@RequestParam String userId, @RequestParam int page) {
        return new JSONResult(homeService.queryMyFans(userId, page));
    }

    @RequestMapping(value = "/read-notify", method = RequestMethod.POST)
    public JSONResult readNotify(@RequestBody Notify notify) {
        return new JSONResult(notifyService.readed4Me(notify));
    }

    @RequestMapping(value = "/read-dynamic", method = RequestMethod.POST)
    public JSONResult readNotify(@RequestBody Feeds feeds) {
        return new JSONResult(feedsService.readDynamics(feeds));
    }


}
