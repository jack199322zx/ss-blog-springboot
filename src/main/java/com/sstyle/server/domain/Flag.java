package com.sstyle.server.domain;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by ss on 2018/4/7.
 */
@Data
public class Flag {
    private long articleId;
    private int techniqueFlag;
    private int techniqueType;
    private String techniqueInfo;
    private Timestamp createTime;
    private String state;

}
