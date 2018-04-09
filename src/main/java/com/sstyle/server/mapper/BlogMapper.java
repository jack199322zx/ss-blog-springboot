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
    List<Article> queryArticlesByPage(@Param("start") int start,@Param("end") int end);
    List<Article> queryArticlesByPageAndFlag(@Param("start") int start,@Param("end") int end, @Param("tech") Flag flag);
    List<Map> queryTecFlags();
}
