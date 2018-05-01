package com.sstyle.server.service;

import com.sstyle.server.domain.JSONResult;
import com.sstyle.server.domain.StaffBean;
import com.sstyle.server.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Created by ss on 2018/3/24.
 */
public interface UserService {
    User findByUsercode(String userCode); //根据用户名查找用户
    User queryUserInfo(String userId);
    Set<String> findRoles(String username);// 根据用户名查找其角色
    Set<String> findPermissions(String staffId);// 根据用户名查找其权限
    Map<String, Integer> queryUserFavoriteAndFollow(String userId, String articleId, String authorId);
    int saveFollowById(String authorId, String followerId);
    int cancelFollowById(String authorId, String followerId);
    int saveFavoriteByArticleId(String articleId, String userId);
    int cancelFavoriteByArticleId(String articleId, String userId);
    JSONResult uploadAvatar(String map, HttpServletRequest request) throws IOException;
}
