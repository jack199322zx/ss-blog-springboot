package com.sstyle.server.service;

import com.sstyle.server.domain.StaffBean;
import com.sstyle.server.domain.User;

import java.util.Set;

/**
 * Created by ss on 2018/3/24.
 */
public interface UserService {
    public User findByUsername(String username); //根据用户名查找用户
    public Set<String> findRoles(String username);// 根据用户名查找其角色
    public Set<String> findPermissions(String staffId);// 根据用户名查找其权限
}
