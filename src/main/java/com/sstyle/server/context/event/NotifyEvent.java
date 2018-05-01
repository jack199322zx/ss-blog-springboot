package com.sstyle.server.context.event;

import lombok.Builder;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

/**
 * @author ss on 2018/4/30.
 */
@Data
public class NotifyEvent extends ApplicationEvent {
	private static final long serialVersionUID = -4261382494171476390L;
	
	private String fromUserId;
    private String toUserId;
    private int eventType;
    private String associateId;

    public NotifyEvent(Object source) {
        super(source);
    }
}
