package com.sstyle.server.domain;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ss on 2018/4/7.
 */
@Data
public class Flag implements Serializable{
    private long articleId;
    private int flagId;
    private int flagDistrict;
    private int flagType;
    private String flagTypeDesc;
    private String flagInfo;
    private Timestamp createTime;
    private String state;

}
