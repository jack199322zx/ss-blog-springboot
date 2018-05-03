package com.sstyle.server.web.cache;

import com.sstyle.server.domain.Article;
import com.sstyle.server.domain.Flag;
import com.sstyle.server.mapper.BlogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author ss
 * @Date 2018/5/3 17:53
 */
@Component
@CacheConfig(cacheNames = "flagCaches")
public class FlagCache {
    private Logger logger = LoggerFactory.getLogger(ArticleCache.class);

    @Autowired
    private BlogMapper blogMapper;

    @Cacheable
    public List<Flag> queryAllFlags() {
        return blogMapper.queryTecFlags();
    }
}
