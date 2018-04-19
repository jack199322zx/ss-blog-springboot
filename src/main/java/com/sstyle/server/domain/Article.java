package com.sstyle.server.domain;

import lombok.Data;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by ss on 2018/4/5.
 */
@Data
public class Article implements Serializable{

    private long articleId;
    private User user;
    private String articleTitle;
    private String articleSign;
    private String articleDesc;
    private int articleType;
    private String articleImg;
    private Timestamp createTime;
    private Timestamp updateTime;
    private int commentsNum;
    private int favoriteNum;
    private int viewNum;
    private int authorRec;
    private List<Flag> flagList;
}
