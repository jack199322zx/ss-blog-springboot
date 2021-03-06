package com.sstyle.server.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@AllArgsConstructor
@NoArgsConstructor
public class Article implements Serializable{

    private String articleId;
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

    public Article(String articleId,String articleTitle,String articleDesc,String articleImg,String articleSign,
    Timestamp createTime,Timestamp updateTime,List<Flag> flagList,int viewNum,int commentsNum,int favoriteNum) {
        this.articleId = articleId;
        this.articleTitle = articleTitle;
        this.articleDesc = articleDesc;
        this.articleImg = articleImg;
        this.articleSign = articleSign;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.flagList = flagList;
        this.viewNum = viewNum;
        this.commentsNum = commentsNum;
        this.favoriteNum = favoriteNum;
    }
}
