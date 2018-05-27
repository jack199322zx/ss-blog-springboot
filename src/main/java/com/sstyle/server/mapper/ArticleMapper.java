package com.sstyle.server.mapper;

import com.sstyle.server.domain.Article;
import com.sstyle.server.domain.Flag;
import com.sstyle.server.domain.Mavon;
import com.sstyle.server.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by ss on 2018/4/19.
 */
public interface ArticleMapper {
    int saveFavoriteById(String articleId);
    int cancelFavoriteById(String articleId);
    int saveArticle(@Param("article") Article article,@Param("user") User user);
    int saveArticleMarkdown(@Param("markdown")String markdown,@Param("articleId") String articleId);
    int saveFlagByArticle(@Param("articleId") String articleId,@Param("flagId") int flagId);
    List<Flag> queryFlagByDist(int dist);
    int deleteArticleById(String articleId);
}
