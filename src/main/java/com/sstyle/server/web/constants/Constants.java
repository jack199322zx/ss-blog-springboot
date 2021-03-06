package com.sstyle.server.web.constants;

/**
 * Created by ss on 2018/4/30.
 */
public class Constants {

    public static final int NOTIFY_EVENT_FAVOR_POST = 1; // 有人喜欢了你的文章

    public static final int NOTIFY_EVENT_FOLLOW = 2; // 有人关注了你

    public static final int NOTIFY_EVENT_COMMENT = 3; // 有人评论了你的文章

    public static final int NOTIFY_EVENT_COMMENT_REPLY = 4; // 有人回复了你

    public static final int FEEDS_TYPE_POST = 1; // 动态类型 - 发布文章

    /**
     * 未读
     */
    public static final int UNREAD = 0;

    /**
     * 已读
     */
    public static final int READED = 1;
    public static final int TOKEN_KEY_EXPIRE_TIME = 60 * 60 * 5 * 100;
    public static final String AUTHORIZATION_HEADER = "Authorization"; //请求头

    public static final int ARTICLE_PAGE_SIZE = 6; //分页大小
    public static final int HOME_PAGE_SIZE = 15;
//    public static final String IMG_PREFIX = "http://localhost:8988/ss-server/"; //图片存储路径前缀
    public static final String IMG_PREFIX = "http://www.52nino.cn/";
    public static final String PRIMARY_USER_ROLE_ID = "40002"; //普通用户
    public static final String UNACTIVE_USER_ROLE_ID = "40004"; //未激活用户
    public static final String ELASTICSEARCH_ADDRESS = "127.0.0.1"; //ES 的配置
    public static final String ELASTICSEARCH_PORT = "9300";
    public static final String OPEN_QQ_CHAT = "tencent://message/?uin=461333622&Site=在线客服&Menu=yes";
}
