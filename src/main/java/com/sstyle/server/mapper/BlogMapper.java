package com.sstyle.server.mapper;

import com.sstyle.server.domain.Article;
import com.sstyle.server.domain.Flag;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by ss on 2018/4/5.
 */
public interface BlogMapper {
    List<Article> queryArticles();
    List<Article> queryArticlesByPageAndDist(@Param("start") int start,@Param("end") int end, @Param("dist") int dist);
    List<Article> queryArticlesByPageAndFlag(@Param("start") int start,@Param("end") int end, @Param("id") int id);
    List<Flag> queryTecFlags();
    List<Article> queryAllArticlesByDist(int dist);
    Article queryArticleDetailById(String articleId);
    int queryPublishArticleNum(long userId);
    int updateViewNum(@Param("viewNum") int viewNum, @Param("articleId") String articleId);
}
