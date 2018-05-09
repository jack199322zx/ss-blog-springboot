package com.sstyle.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sstyle.server.context.SpringContextHolder;
import com.sstyle.server.context.event.NotifyEvent;
import com.sstyle.server.domain.JSONResult;
import com.sstyle.server.domain.User;
import com.sstyle.server.mapper.ArticleMapper;
import com.sstyle.server.mapper.UserMapper;
import com.sstyle.server.service.FeedsService;
import com.sstyle.server.service.NotifyService;
import com.sstyle.server.service.UserService;
import com.sstyle.server.utils.*;
import com.sstyle.server.utils.MapUtils;
import com.sstyle.server.web.constants.Constants;
import org.apache.commons.collections.*;
import org.hibernate.validator.internal.constraintvalidators.bv.past.PastValidatorForReadableInstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Created by ss on 2018/3/24.
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private FeedsService feedsService;
    @Autowired
    private NotifyService notifyService;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User findByUsercode(String username) {
        return userMapper.queryUsername(username);
    }

    @Override
    public Set<String> findRoles(String userCode) {
        return userMapper.queryRoles(userCode);
    }

    @Override
    public Set<String> findPermissions(String userCode) {
        return userMapper.queryPermissions(userCode);
    }

    @Override
    public Map<String, Integer> queryUserFavoriteAndFollow(String userId, String articleId, String authorId) {
        Integer favorite = userMapper.queryUserFavorite(userId, articleId);
        Integer follow = userMapper.queryUserFollow(authorId, userId);
        return MapUtils.of("favorite", favorite, "follow", follow);
    }

    @Override
    public int saveFollowById(String authorId, String followerId) {
        NotifyEvent notifyEvent = new NotifyEvent("NotifyEvent");
        notifyEvent.setEventType(Constants.NOTIFY_EVENT_FOLLOW);
        notifyEvent.setFromUserId(followerId);
        notifyEvent.setToUserId(authorId);
        SpringContextHolder.publishEvent(notifyEvent);
        return userMapper.saveUserFollow(authorId, followerId);
    }

    @Override
    @Transactional
    public int cancelFollowById(String authorId, String followerId) {
        //取消关注时，删除此人的动态
        feedsService.deleteByAuthorId(followerId, authorId);
        return userMapper.cancelUserFollow(authorId, followerId);
    }

    @Override
    public JSONResult uploadAvatar(String base64, HttpServletRequest request) throws IOException{
        if (base64 == null) {
            return new JSONResult("failed");
        }
        String backHash = QiniuUtil.uploadBase64(base64);
        logger.info("backHash========={}", backHash);
        String userId = ThreadContext.getStaffId();
        userMapper.saveUserAvatar(backHash, userId);
        //更新redis，用户上传的头像
        String authToken = request.getHeader(Constants.AUTHORIZATION_HEADER);
        String json = RedisClient.getAndDel(authToken);
        User user = JSONObject.parseObject(json, User.class);
        user.setAvatar(backHash);
        RedisClient.setex(authToken, JSON.toJSONString(user), Constants.TOKEN_KEY_EXPIRE_TIME);
        return new JSONResult(backHash);
    }
    @Override
    @Transactional
    public int saveFavoriteByArticleId(String articleId, String userId) {
        userMapper.saveUserFavorite(userId, articleId);
        NotifyEvent notifyEvent = new NotifyEvent("NotifyEvent");
        notifyEvent.setAssociateId(articleId);
        notifyEvent.setEventType(Constants.NOTIFY_EVENT_FAVOR_POST);
        notifyEvent.setFromUserId(userId);
        //此处不知道作者，让通知事件处理器补全
        SpringContextHolder.publishEvent(notifyEvent);
        return articleMapper.saveFavoriteById(articleId);
    }

    @Override
    @Transactional
    public int cancelFavoriteByArticleId(String articleId, String userId) {
        // 取消用户喜欢文章的通知
        notifyService.deleteNotifyByCancelFavorite(userId, articleId);
        userMapper.cancelUserFavorite(userId, articleId);
        return articleMapper.cancelFavoriteById(articleId);
    }

    @Override
    public User queryUserInfo(String userId) {
        return userMapper.queryUserInfo(userId);
    }

    @Override
    public int saveUserInfo(String userAddress, String nickName) {
        return userMapper.saveUserInfo(userAddress, nickName, ThreadContext.getStaffId());
    }

    @Override
    public int saveUserNewPassword(String newPassword, String oldPassword) {
        User user = userMapper.queryUsername(ThreadContext.getStaffCode());
        if (!AESUtil.aesDecode(user.getPassword()).equals(oldPassword)) {
            return 0;
        }
        return userMapper.saveUserPwd(AESUtil.aesEncode(newPassword), ThreadContext.getStaffId());
    }

}
