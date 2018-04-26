package com.sstyle.server.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @Author ss
 * @Date 2018/4/10 16:51
 */
public interface RegisterMapper {
    int saveUserByEmail(@Param("params") Map params);
    int saveUserByPhone(@Param("params") Map params);
    int saveUserRole(@Param("userId") String userId, @Param("roleId") String roleId);
    int updateActiveUser(String userId);
}
