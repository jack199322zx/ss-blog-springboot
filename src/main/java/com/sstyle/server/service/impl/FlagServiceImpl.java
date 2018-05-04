package com.sstyle.server.service.impl;

import com.sstyle.server.domain.Flag;
import com.sstyle.server.mapper.BlogMapper;
import com.sstyle.server.service.FlagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author ss
 * @Date 2018/5/4 14:25
 */
@Service
@CacheConfig(cacheNames = "flagCaches")
public class FlagServiceImpl implements FlagService{

    @Autowired
    private BlogMapper blogMapper;

    @Override
    @Cacheable
    public List<Flag> queryAllFlags() {
        return blogMapper.queryTecFlags();
    }
}
