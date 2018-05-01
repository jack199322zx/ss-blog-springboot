/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.sstyle.server.context.event;

import lombok.Data;
import org.springframework.context.ApplicationEvent;

/**
 * @author ss on 2018/4/18.
 */
@Data
public class FeedsEvent extends ApplicationEvent {
	private static final long serialVersionUID = 3220416026013707101L;

	private String fromUserId;
    private String associateId;
    private int privacy;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the component that published the event (never {@code null})
     */
    public FeedsEvent(Object source) {
        super(source);
    }
}
