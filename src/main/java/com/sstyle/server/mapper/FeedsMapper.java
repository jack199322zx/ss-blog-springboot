package com.sstyle.server.mapper;

import com.sstyle.server.domain.Feeds;

import java.util.List;

/**
 * Created by ss on 2018/5/1.
 */
public interface FeedsMapper {
    int saveFeeds(List<Feeds> feedsList);
}
