package com.sstyle.server.mapper;

import com.sstyle.server.domain.*;

import java.util.List;

/**
 * Created by ss on 2018/5/1.
 */
public interface HomeMapper {
    List<Feeds> queryDynamicsById(String userId);
    List<Article> queryMyArticlesById(String userId);
    List<Comment> queryMyCommentsById(String userId);
    List<Notify> queryMyNotifyById(String userId);
    List<Article> queryMyFavoritesById(String userId);
    List<User> queryMyFollow(String userId);
    List<User> queryMyFans(String userId);
}
