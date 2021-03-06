package com.sstyle.server.controller;

import com.sstyle.server.domain.JSONResult;
import com.sstyle.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

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
    public JSONResult queryUserFavorite(@RequestParam String articleId, @RequestParam String userId, @RequestParam String authorId) {
        return new JSONResult(userService.queryUserFavoriteAndFollow(userId, articleId, authorId));
    }

    @RequestMapping(value = "/save-follow", method = RequestMethod.POST)
    public JSONResult saveUserFollow(@RequestParam String authorId, @RequestParam String followerId) {
        return new JSONResult(userService.saveFollowById(authorId, followerId));
    }

    @RequestMapping(value = "/cancel-follow", method = RequestMethod.POST)
    public JSONResult cancelUserFollow(@RequestParam String authorId, @RequestParam String followerId) {
        return new JSONResult(userService.cancelFollowById(authorId, followerId));
    }

    @RequestMapping(value = "/save-avatar", method = RequestMethod.POST)
    public JSONResult cancelUserFollow(@RequestParam String avatar, HttpServletRequest request) throws IOException{
        return userService.uploadAvatar(avatar, request);
    }

    @RequestMapping(value = "/save-favorite", method = RequestMethod.POST)
    public JSONResult saveFavorite(@RequestParam String articleId, @RequestParam String userId) {
        return new JSONResult(userService.saveFavoriteByArticleId(articleId, userId));
    }

    @RequestMapping(value = "/cancel-favorite", method = RequestMethod.POST)
    public JSONResult cancelFavorite(@RequestParam String articleId, @RequestParam String userId) {
        return new JSONResult(userService.cancelFavoriteByArticleId(articleId, userId));
    }

    @RequestMapping(value = "/query-user-info", method = RequestMethod.POST)
    public JSONResult queryUserInfo(@RequestParam String userId) {
        return new JSONResult(userService.queryUserInfo(userId));
    }

    @RequestMapping(value = "/save-user-info", method = RequestMethod.POST)
    public JSONResult saveUserInfo(@RequestParam String userAddress, @RequestParam String nickName) {
        return new JSONResult(userService.saveUserInfo(userAddress, nickName));
    }

    @RequestMapping(value = "/save-user-pwd", method = RequestMethod.POST)
    public JSONResult saveUserPassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        return new JSONResult(userService.saveUserNewPassword(newPassword, oldPassword));
    }

}
