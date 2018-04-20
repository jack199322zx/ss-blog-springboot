package com.sstyle.server.service.impl;

import com.sstyle.server.domain.User;
import com.sstyle.server.mapper.UserMapper;
import com.sstyle.server.service.UserService;
import com.sstyle.server.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

/**
 * Created by ss on 2018/3/24.
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

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
}
