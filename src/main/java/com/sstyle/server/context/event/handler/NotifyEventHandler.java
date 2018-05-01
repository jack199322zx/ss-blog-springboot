package com.sstyle.server.context.event.handler;


import com.sstyle.server.context.event.NotifyEvent;
import com.sstyle.server.domain.Article;
import com.sstyle.server.domain.Notify;
import com.sstyle.server.mapper.BlogMapper;
import com.sstyle.server.service.BlogService;
import com.sstyle.server.service.NotifyService;
import com.sstyle.server.web.constants.Constants;
import org.n3r.idworker.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author ss on 2018/4/30.
 */
@Component
public class NotifyEventHandler implements ApplicationListener<NotifyEvent> {
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private BlogMapper blogMapper;

    @Async
    @Override
    public void onApplicationEvent(NotifyEvent event) {
        Notify nt = new Notify();
        nt.setNotifyId(String.valueOf(Id.next()));
        nt.setAssociationId(event.getAssociateId());
        nt.setFromId(event.getFromUserId());
        nt.setEventType(event.getEventType());

        switch (event.getEventType()) {
            case Constants.NOTIFY_EVENT_COMMENT:
                Article a = blogMapper.queryArticleDetailById(event.getAssociateId());
                nt.setToId(a.getUser().getId());
                // 自增评论数
                blogMapper.updateCommentsNum(a.getCommentsNum()+ 1, a.getArticleId());
                break;
            case Constants.NOTIFY_EVENT_FAVOR_POST:
                Article a2 = blogMapper.queryArticleDetailById(event.getAssociateId());
                nt.setToId(a2.getUser().getId());
                break;
            case Constants.NOTIFY_EVENT_COMMENT_REPLY:
                Article a3 = blogMapper.queryArticleDetailById(event.getAssociateId());
                nt.setToId(event.getToUserId());
                // 自增评论数
                blogMapper.updateCommentsNum(a3.getCommentsNum()+ 1, a3.getArticleId());
                break;
            default:
                nt.setToId(event.getToUserId());
        }
        notifyService.send(nt);
    }
}
