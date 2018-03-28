package com.sstyle.server.domain;

import lombok.Data;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Created by ss on 2018/3/27.
 */
@Data
public class Right implements Serializable{

    private Integer id;
    private String name;
    private String desc;
    private String type;
    private String state;
    private DateTime createTime;
    private DateTime updateTime;

}
