/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.sstyle.server.context.event.handler;

import com.sstyle.server.context.event.FeedsEvent;
import com.sstyle.server.domain.Feeds;
import com.sstyle.server.service.FeedsService;
import com.sstyle.server.web.constants.Constants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * @author ss on 2018/4/30.
 */
@Component
public class FeedsEventHandler implements ApplicationListener<FeedsEvent> {
    private Logger log = Logger.getLogger(getClass());

    @Autowired
    private FeedsService feedsService;

    @Async
    @Override
    public void onApplicationEvent(FeedsEvent event) {
        if (event == null) {
            return;
        }
        // 创建动态对象
        Feeds feeds = new Feeds();
        feeds.setFeedsType(Constants.FEEDS_TYPE_POST);
        feeds.setFromId(event.getFromUserId());
        feeds.setAssociationId(event.getAssociateId());

        int ret = feedsService.add(feeds);

        log.debug(MessageFormat.format("成功派发 {0} 条动态!", ret));
    }
}
