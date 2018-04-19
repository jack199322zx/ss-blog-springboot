package com.sstyle.server.mapper;

/**
 * Created by ss on 2018/4/19.
 */
public interface ArticleMapper {
    int saveFavoriteById(long articleId);
    int cancelFavoriteById(long articleId);
}
