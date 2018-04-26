package com.sstyle.server.service;

import com.sstyle.server.domain.Right;

import java.util.List;

/**
 * Created by ss on 2018/3/27.
 */
public interface RightService {
    List<Right> findRights(String userId);
}
