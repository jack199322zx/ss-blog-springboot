package com.sstyle.server.mapper;

import com.sstyle.server.domain.Article;
import com.sstyle.server.domain.Mavon;
import com.sstyle.server.domain.User;
import org.apache.ibatis.annotations.Param;

/**
 * Created by ss on 2018/4/19.
 */
public interface ArticleMapper {
    int saveFavoriteById(long articleId);
    int cancelFavoriteById(long articleId);
    int saveArticle(@Param("article") Article article,@Param("user") User user);
}
