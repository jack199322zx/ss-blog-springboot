package com.sstyle.server.mapper;

import com.sstyle.server.domain.StaffBean;
import com.sstyle.server.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * Created by ss on 2018/3/24.
 */

public interface UserMapper {
    User queryUsername(String username);
    int saveUserFavorite(@Param("userId") long userId, @Param("articleId") long articleId);
    int cancelUserFavorite(@Param("userId") long userId, @Param("articleId") long articleId);
//    Set<String> queryRoles(String username);
//    Set<String> queryPermissions(String staffId);
}
