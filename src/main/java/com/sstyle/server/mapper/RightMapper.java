package com.sstyle.server.mapper;

import com.sstyle.server.domain.Right;

import java.util.List;

/**
 * Created by ss on 2018/3/27.
 */
public interface RightMapper {
    List<Right> queryRights(String userId);

}
