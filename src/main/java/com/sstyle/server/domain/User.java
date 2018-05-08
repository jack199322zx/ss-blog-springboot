package com.sstyle.server.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author ss
 * @Date 2018/3/27 16:21
 */
@Data
@NoArgsConstructor
public class User implements Serializable{

    private String id;
    private String password;
    private String userName;
    private String userCode;
    private String telephone;
    private String email;
    private String address;
    private String avatar;
    private DateTime createTime;
    private DateTime updateTime;
    private Map<String, Object> notifyAndDynamics;


}
