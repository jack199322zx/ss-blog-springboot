package com.sstyle.server.domain;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by ss on 2018/4/30.
 */
@Data
public class Notify {

    private String notifyId;
    private String associationId;
    private String fromId;
    private String toId;
    private Timestamp createTime;
    private String state;
    private int eventType;
}
