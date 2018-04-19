package com.sstyle.server.service.impl;

import com.sstyle.server.domain.StaffBean;
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

}
