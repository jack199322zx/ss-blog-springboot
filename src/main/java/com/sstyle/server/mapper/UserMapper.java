package com.sstyle.server.mapper;

import com.sstyle.server.domain.StaffBean;
import com.sstyle.server.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * Created by ss on 2018/3/24.
 */

public interface UserMapper {
    User queryUsername(String username);

    User queryUserInfo(String userId);

    int saveUserFavorite(@Param("userId") String userId, @Param("articleId") String articleId);

    int cancelUserFavorite(@Param("userId") String userId, @Param("articleId") String articleId);

    int queryUserFavorite(@Param("userId") String userId, @Param("articleId") String articleId);

    int queryUserFollow(@Param("authorId") String authorId, @Param("userId") String userId);

    int saveUserFollow(@Param("authorId") String authorId, @Param("followerId") String followerId);

    int cancelUserFollow(@Param("authorId") String authorId, @Param("followerId") String followerId);

    int saveUserAvatar(@Param("avatar") String avatar,@Param("userId") String userId);

    List<User> queryFollowerByUserId(String userId);
    //    Set<String> queryRoles(String username);
//    Set<String> queryPermissions(String staffId);
}
