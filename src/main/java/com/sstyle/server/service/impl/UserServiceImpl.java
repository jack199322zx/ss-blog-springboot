package com.sstyle.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sstyle.server.domain.JSONResult;
import com.sstyle.server.domain.User;
import com.sstyle.server.mapper.UserMapper;
import com.sstyle.server.service.UserService;
import com.sstyle.server.utils.MapUtils;
import com.sstyle.server.utils.QiniuUtil;
import com.sstyle.server.utils.RedisClient;
import com.sstyle.server.utils.ThreadContext;
import org.hibernate.validator.internal.constraintvalidators.bv.past.PastValidatorForReadableInstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private int EXPIRE_TIME = 100 * 60 * 60 * 5;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User findByUsercode(String username) {
        return userMapper.queryUsername(username);
    }

    @Override
    public Set<String> findRoles(String username) {
        return null;
    }

    @Override
    public Set<String> findPermissions(String staffId) {
        return null;
    }

    @Override
    public Map<String, Integer> queryUserFavoriteAndFollow(long userId, long articleId, long authorId) {
        Integer favorite = userMapper.queryUserFavorite(userId, articleId);
        Integer follow = userMapper.queryUserFollow(authorId, userId);
        return MapUtils.of("favorite", favorite, "follow", follow);
    }

    @Override
    public int saveFollowById(long authorId, long followerId) {
        return userMapper.saveUserFollow(authorId, followerId);
    }

    @Override
    public int cancelFollowById(long authorId, long followerId) {
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
        String authToken = request.getHeader(AUTHORIZATION_HEADER);
        String json = RedisClient.getAndDel(authToken);
        User user = JSONObject.parseObject(json, User.class);
        user.setAvatar(backHash);
        RedisClient.setex(authToken, JSON.toJSONString(user), EXPIRE_TIME);
        return new JSONResult(backHash);
    }
}
