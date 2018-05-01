package com.sstyle.server.service.impl;

import com.sstyle.server.domain.Notify;
import com.sstyle.server.mapper.NotifyMapper;
import com.sstyle.server.service.NotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ss on 2018/4/30.
 */
@Service
public class NotifyServiceImpl implements NotifyService{

    @Autowired
    private NotifyMapper notifyMapper;

    @Override
    public Notify findByToId(String toId) {
        return null;
    }

    @Override
    public void send(Notify notify) {
        if (notify == null) {
            return;
        }
        notifyMapper.saveNotify(notify);
    }

    @Override
    public int unread4Me(long ownId) {
        return 0;
    }

    @Override
    public void readed4Me(long ownId) {

    }
}
