package com.sstyle.server.domain;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by ss on 2018/4/30.
 */
@Data
public class Feeds {

    private String feedsId;
    private String fromId;
    private String toId;
    private String associationId;
    private String state;
    private Timestamp createTime;
    private int feedsType;
}
