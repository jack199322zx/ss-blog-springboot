package com.sstyle.server.service;

import com.sstyle.server.domain.StaffBean;
import com.sstyle.server.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.Map;
import java.util.Set;

/**
 * Created by ss on 2018/3/24.
 */
public interface UserService {
    User findByUsercode(String userCode); //根据用户名查找用户
    Set<String> findRoles(String username);// 根据用户名查找其角色
    Set<String> findPermissions(String staffId);// 根据用户名查找其权限
    Map<String, Integer> queryUserFavoriteAndFollow(long userId, long articleId, long authorId);
    int saveFollowById(long authorId, long followerId);
    int cancelFollowById(long authorId, long followerId);
}
