package com.sstyle.server.domain;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Author ss
 * @Date 2018/4/27 16:47
 */
@Data
public class Comment implements Serializable{

    private String commentId;
    private String articleId;
    private String userId;
    private String commentContent;
    private Timestamp createTime;
    private String state;
    private String receiveCommentId;
    private Comment receiveComment;
    private User user;
}
