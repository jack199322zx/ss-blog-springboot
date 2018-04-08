package com.sstyle.server.service.impl;

import com.sstyle.server.domain.Article;
import com.sstyle.server.domain.Flag;
import com.sstyle.server.mapper.BlogMapper;
import com.sstyle.server.service.BlogService;
import com.sstyle.server.utils.MapUtils;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by ss on 2018/4/5.
 */
@Service
public class BlogServiceImpl implements BlogService{

    @Autowired
    private BlogMapper blogMapper;

    private int pageSize = 6;

    private Logger logger = LoggerFactory.getLogger(BlogServiceImpl.class);

    public Map<String, Object> initBlog() {
        List<Article> articleList = blogMapper.queryArticles();
        List<Map> flagList = blogMapper.queryTecFlags();
        List<Article> viewNumSortedList = articleList.stream()
                .sorted((article1, article2) -> article2.getViewNum() - article1.getViewNum())
                .limit(6)
                .collect(Collectors.toList());
        List<Article> createTimeSortedList = articleList.stream()
                .sorted((article1, article2) -> article2.getCreateTime().compareTo(article1.getCreateTime()))
                .limit(6)
                .collect(Collectors.toList());
        List<Article> authorRecList = articleList.stream().filter(article -> article.getAuthorRec() == 1).collect(Collectors.toList());
        return MapUtils.of("articleList", articleList.subList(0, 6), "flagList", flagList,
                "viewNumSortedList", viewNumSortedList,
                "createTimeSortedList", createTimeSortedList,
                "authorRecList", authorRecList);
    }

    @Override
    public Map<String, Object> initBlogList(Map params) {
        int page = MapUtils.getInt(params, "page");
        Map receiveFlag = MapUtils.getMapForce(params, "receiveFlag");
        int start = page * pageSize;
        int end = (page + 1) * pageSize - 1;
        List<? extends Object> articleList = new ArrayList<>();
        if (receiveFlag != null) {
            Flag flag = new Flag();
            flag.setTechniqueFlag((int) receiveFlag.get("techniqueFlag"));
            flag.setTechniqueType((int) receiveFlag.get("techniqueType"));
            articleList = blogMapper.queryArticlesByPageAndFlag(start, end, flag);
        }else {
            articleList = blogMapper.queryArticlesByPage(start, end);
        }
        List<Map> flagList = blogMapper.queryTecFlags();
        return MapUtils.of("articleList", articleList, "flagList", flagList);
    }
}
