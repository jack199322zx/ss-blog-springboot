package com.sstyle.server.domain;

import lombok.Data;
import sun.text.resources.cldr.lg.FormatData_lg;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by ss on 2018/4/21.
 */
@Data
public class Mavon {

    private String title;
    private int channelId;
    private String markdown;
    private String sign;
    private List<Flag> flagList;
    private User user;

}
