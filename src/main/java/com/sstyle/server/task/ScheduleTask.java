package com.sstyle.server.task;

import com.sstyle.server.service.SearchService;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;


/**
 * Created by ss on 2018/5/5.
 */

@Configuration
@Component
@EnableScheduling
public class ScheduleTask {

    @Autowired
    private TransportClient client;
    @Autowired
    private SearchService searchService;
    private Logger logger = LoggerFactory.getLogger(ScheduleTask.class);

    public void execute() {
        logger.info("开始异步执行更新索引=============");
        Future<String> future = searchService.deleteIndex();
        searchService.generateEsIndex(future);
        logger.info("更新索引结束================");
    }
}
