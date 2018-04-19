package com.sstyle.server.domain;

import lombok.Data;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * @Author ss
 * @Date 2018/3/27 16:21
 */
@Data
public class User implements Serializable{

    private Long id;
    private String password;
    private String userName;
    private String userCode;
    private String telephone;
    private String email;
    private String address;
    private DateTime createTime;
    private DateTime updateTime;
}
