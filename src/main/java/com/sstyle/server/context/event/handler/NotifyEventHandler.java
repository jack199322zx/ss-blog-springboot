package com.sstyle.server.context.event.handler;


import com.sstyle.server.context.event.NotifyEvent;
import com.sstyle.server.domain.Article;
import com.sstyle.server.domain.Notify;
import com.sstyle.server.mapper.BlogMapper;
import com.sstyle.server.service.BlogService;
import com.sstyle.server.service.NotifyService;
import com.sstyle.server.web.constants.Constants;
import org.apache.commons.lang3.StringUtils;
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
        String fromId = event.getFromUserId();
        nt.setFromId(fromId);
        nt.setEventType(event.getEventType());
        String associationId = event.getAssociateId();
        String toId;
        if (StringUtils.isNotEmpty(associationId)) {
            nt.setAssociationId(associationId);
            Article a = blogMapper.queryArticleDetailById(associationId);
            switch (event.getEventType()) {
                case Constants.NOTIFY_EVENT_COMMENT:
                    toId = a.getUser().getId();
                    nt.setCommentId(event.getCommentId());
                    // 自增评论数
                    blogMapper.updateCommentsNum(a.getCommentsNum()+ 1, a.getArticleId());
                    // 评论自己的文章，不发送通知
                    if (fromId.equals(toId)) return;
                    break;
                case Constants.NOTIFY_EVENT_FAVOR_POST:
                    toId = a.getUser().getId();
                    //喜欢自己的文章，不发送通知
                    if (fromId.equals(toId)) return;
                    break;
                case Constants.NOTIFY_EVENT_COMMENT_REPLY:
                    toId = event.getToUserId();
                    nt.setCommentId(event.getCommentId());
                    // 自增评论数
                    blogMapper.updateCommentsNum(a.getCommentsNum()+ 1, a.getArticleId());
                    if (fromId.equals(toId)) return;
                    break;
                default:
                    toId = event.getToUserId();
            }
        }else {
            toId = event.getToUserId();
        }
        nt.setToId(toId);
        notifyService.send(nt);
    }
}
