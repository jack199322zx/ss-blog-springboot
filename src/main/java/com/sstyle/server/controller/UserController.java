package com.sstyle.server.controller;

import com.sstyle.server.domain.JSONResult;
import com.sstyle.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author ss
 * @Date 2018/4/20 12:03
 */
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/query-favorite-follow", method = RequestMethod.POST)
    public JSONResult queryUserFavorite(@RequestParam long articleId, @RequestParam long userId, @RequestParam long authorId) {
        return new JSONResult(userService.queryUserFavoriteAndFollow(userId, articleId, authorId));
    }

    @RequestMapping(value = "/save-follow", method = RequestMethod.POST)
    public JSONResult saveUserFollow(@RequestParam long authorId, @RequestParam long followerId) {
        return new JSONResult(userService.saveFollowById(authorId, followerId));
    }

    @RequestMapping(value = "/cancel-follow", method = RequestMethod.POST)
    public JSONResult cancelUserFollow(@RequestParam long authorId, @RequestParam long followerId) {
        return new JSONResult(userService.cancelFollowById(authorId, followerId));
    }
}
