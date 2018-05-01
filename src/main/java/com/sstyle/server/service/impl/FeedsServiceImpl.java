package com.sstyle.server.service.impl;

import com.sstyle.server.domain.Feeds;
import com.sstyle.server.domain.User;
import com.sstyle.server.mapper.FeedsMapper;
import com.sstyle.server.mapper.UserMapper;
import com.sstyle.server.service.FeedsService;
import org.n3r.idworker.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ss on 2018/4/30.
 */
@Service
public class FeedsServiceImpl implements FeedsService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FeedsMapper feedsMapper;

    @Override
    @Transactional
    public int add(Feeds feeds) {
        List<Feeds> feedsList = new ArrayList<>();
        List<User> userList = userMapper.queryFollowerByUserId(feeds.getFromId());
        if (userList != null && userList.size()> 0) {
            userList.stream().forEach(user -> {
                Feeds f = new Feeds();
                f.setFeedsId(String.valueOf(Id.next()));
                f.setToId(user.getId());
                f.setFromId(feeds.getFromId());
                f.setAssociationId(feeds.getAssociationId());
                f.setFeedsType(feeds.getFeedsType());
                feedsList.add(f);
            });
            return feedsMapper.saveFeeds(feedsList);
        }
        return 0;
    }

    @Override
    public int deleteByAuthorId(long ownId, long authorId) {
        return 0;
    }

    @Override
    public List<Feeds> findUserFeeds(String userId) {
        return null;
    }

    @Override
    public void deleteByTarget(long postId) {

    }
}
