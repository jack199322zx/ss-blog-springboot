package com.sstyle.server.service.impl;

import com.sstyle.server.domain.Article;
import com.sstyle.server.mapper.SearchMapper;
import com.sstyle.server.service.ArticleService;
import com.sstyle.server.service.SearchService;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.hibernate.validator.internal.constraintvalidators.bv.past.PastValidatorForReadableInstant;
import org.n3r.idworker.Id;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;

/**
 * Created by ss on 2018/5/5.
 */
@Service
public class SearchServiceImpl implements SearchService{

    @Autowired
    private SearchMapper searchMapper;
    @Autowired
    private TransportClient client;
    @Autowired
    private ArticleService articleService;
    private Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Override
    public List<String> findKeywords(String keywords) {
        return searchMapper.queryKeywords(keywords);
    }

    @Override
    @Transactional
    public List<Article> saveKeywords(String keywords) {
        List<Article> articleList = new ArrayList<>();
        int row = searchMapper.saveKeywords(keywords, String.valueOf(Id.next()));
        if (row != 1) return articleList;
        BoolQueryBuilder queryBuilder = boolQuery().must(QueryBuilders.multiMatchQuery(keywords, "articleTitle", "articleDesc", "articleSign", "articleUser"));
        SearchResponse searchResponse = client.prepareSearch("esindex")
                .setTypes("article")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .highlighter(new HighlightBuilder().preTags("<span>").postTags("</span>").field("articleDesc").field("articleTitle"))
                .setFrom(0).setSize(60)
                .setExplain(true)
                .get();
        SearchHits hits = searchResponse.getHits();
        long count = hits.getTotalHits();
        for (SearchHit hit : hits) {
            Map<String, Object> hitSource = hit.getSource();
            HighlightField articleTitleHightLight = hit.getHighlightFields().get("articleTitle");
            HighlightField articleDescHightLight = hit.getHighlightFields().get("articleDesc");
            String articleTitle = articleTitleHightLight == null ?
                    hitSource.get("articleTitle").toString() : articleTitleHightLight.getFragments()[0].toString();
            String articleDesc = articleDescHightLight == null ?
                    hitSource.get("articleDesc").toString() : articleDescHightLight.getFragments()[0].toString();
            Article article = new Article(hit.id(), articleTitle, articleDesc);
            articleList.add(article);
        }

        return articleList;
    }

    @Async
    @Override
    public void generateEsIndex (Future<String> future) {
        for(;!future.isDone(););
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        List<Article> articleList = articleService.queryAllArticles();
        logger.info("开始添加索引,articleList={}", articleList);

        XContentBuilder settingsBuilder = null;
        try {
            settingsBuilder = XContentFactory.jsonBuilder()
                    .startObject()
                    .startObject("analysis")
                    .startObject("analyzer")
                    .startObject("optimizeIK")
                    .field("tokenizer", "ik_max_word")
                    .array("filter", "stemmer")
                    .endObject()
                    .endObject()
                    .endObject()
                    .endObject();

            client.admin().indices()
                    .prepareCreate("esindex").setSettings(settingsBuilder).get();

            // 创建mapping
            XContentBuilder mapping = XContentFactory.jsonBuilder().startObject().startObject("article").startObject("properties")
                    .startObject("articleImg").field("type", "string")
                    .field("index", "no")
                    .endObject()
                    .startObject("articleDesc").field("type", "string")
                    .field("analyzer", "optimizeIK").field("search_analyzer", "optimizeIK")
                    .endObject()
                    .startObject("articleSign").field("type", "string")
                    .field("analyzer", "optimizeIK").field("search_analyzer", "optimizeIK")
                    .endObject()
                    .startObject("articleTitle").field("type", "string")
                    .field("analyzer", "optimizeIK").field("search_analyzer", "optimizeIK")
                    .endObject()
                    .startObject("articleFlagList").field("type", "string")
                    .field("analyzer", "optimizeIK").field("search_analyzer", "optimizeIK")
                    .endObject()
                    .startObject("articleUser").field("type", "string")
                    .field("analyzer", "optimizeIK").field("search_analyzer", "optimizeIK")
                    .endObject()
                    .endObject().endObject().endObject();

            PutMappingRequest mappingRequest = Requests.putMappingRequest("esindex").type("article").source(mapping);
            client.admin().indices().putMapping(mappingRequest).actionGet();
            for (Article article : articleList) {

                bulkRequest.add(client.prepareIndex("esindex", "article", article.getArticleId())
                        .setSource(XContentFactory.jsonBuilder()
                                .startObject()
                                .field("articleImg", article.getArticleImg())
                                .field("articleDesc", article.getArticleDesc())
                                .field("articleSign", article.getArticleSign())
                                .field("articleTitle", article.getArticleTitle())
                                .field("articleType", article.getArticleType())
                                .field("articleRec", article.getAuthorRec())
                                .field("articleCommentsNum", article.getCommentsNum())
                                .field("articleCreateTime", article.getCreateTime())
                                .field("articleFavoriteNum", article.getFavoriteNum())
                                .field("articleFlagList", article.getFlagList())
                                .field("articleUser", article.getUser())
                                .field("articleViewNum", article.getViewNum())
                                .field("articleUpdateTime", article.getUpdateTime())
                                .endObject()
                        )
                );
            }
            BulkResponse bulkResponse = bulkRequest.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Async
    @Override
    public Future<String> deleteIndex() {
        logger.info("开始删除索引");
        client.admin().indices().prepareDelete("esindex").get();
        logger.info("删除索引结束===========");
        return new AsyncResult<String>("ok");
    }
}
