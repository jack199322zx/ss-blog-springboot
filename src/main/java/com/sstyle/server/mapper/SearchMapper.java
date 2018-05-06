package com.sstyle.server.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ss on 2018/5/5.
 */
public interface SearchMapper {
    List<String> queryKeywords(String keywords);
    int saveKeywords(@Param("keywords") String keywords, @Param("searchId") String searchId);
}
