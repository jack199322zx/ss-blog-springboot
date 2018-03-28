package com.sstyle.server.service.impl;

import com.sstyle.server.domain.Right;
import com.sstyle.server.mapper.RightMapper;
import com.sstyle.server.service.RightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by ss on 2018/3/27.
 */
@Service
public class RightServiceImpl implements RightService{

    @Autowired
    private RightMapper rightMapper;

    @Override
    public List<Right> findRights(Long userId) {
        return rightMapper.queryRights(userId);
    }
}
