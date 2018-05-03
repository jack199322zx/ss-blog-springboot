package com.sstyle.server.context.event;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.context.ApplicationEvent;

/**
 * @author ss on 2018/4/30.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NotifyEvent extends ApplicationEvent {
	private static final long serialVersionUID = -4261382494171476390L;

	private String commentId;
	private String fromUserId;
    private String toUserId;
    private int eventType;
    private String associateId;

    public NotifyEvent(Object source) {
        super(source);
    }
}
