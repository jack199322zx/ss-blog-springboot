package com.sstyle.server.service.impl;

import com.sstyle.server.domain.User;
import com.sstyle.server.mapper.UserMapper;
import com.sstyle.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public int saveUserFavorite(long userId, long articleId) {
        return userMapper.saveUserFavorite(userId, articleId);
    }

    @Override
    public int cancelUserFavorite(long userId, long articleId) {
        return userMapper.cancelUserFavorite(userId, articleId);
    }
}
