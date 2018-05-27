package com.sstyle.server.mapper;

import com.sstyle.server.domain.Article;
import com.sstyle.server.domain.Flag;
import com.sstyle.server.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by ss on 2018/4/5.
 */
public interface BlogMapper {
    List<Article> queryArticles();
    List<Article> queryArticlesByPageAndFlag(@Param("start") int start,@Param("end") int end, @Param("id") int id);
    List<Flag> queryTecFlags();
    List<Article> queryPageArticlesByDist(int dist);
    Article queryArticleDetailById(String articleId);
    Article queryArticleMarkdownById(String articleId);
    List<Map> queryHotUser();
    int queryPublishArticleNum(String userId);
    int queryCommentsNum(String userId);
    int updateViewNum(@Param("viewNum") int viewNum, @Param("articleId") String articleId);
    int updateCommentsNum(@Param("commentsNum") int commentsNum, @Param("articleId") String articleId);
}
