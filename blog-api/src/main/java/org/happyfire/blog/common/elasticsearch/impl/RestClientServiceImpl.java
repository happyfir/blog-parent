package org.happyfire.blog.common.elasticsearch.impl;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.happyfire.blog.common.elasticsearch.RestClientService;
import org.happyfire.blog.dao.pojo.Article;
import org.happyfire.blog.vo.param.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.happyfire.blog.constants.BlogArticleConstants.Article_Mapping;

@Service
public class RestClientServiceImpl implements RestClientService {

    @Autowired
    private RestHighLevelClient client;
    @Override
    public List<Article> search(PageParams pageParams) {
        int page = pageParams.getPage();
        int pageSize = pageParams.getPageSize();
        List<Article> articleList = null;
        try {
            // 1.准备Request
            SearchRequest request = new SearchRequest(Article_Mapping);
            // 2.发送请求，得到响应
            request.source().query(QueryBuilders.matchQuery("all",pageParams.getCondition()));
//            request.source().sort("create_date", SortOrder.DESC);
            request.source().from((page - 1) * pageSize).size(pageSize);
            // 3.解析响应结果
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            articleList = handleResponse(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return articleList;
    }

    private List<Article> handleResponse(SearchResponse response) {
        List<Article> res = new ArrayList<>();
        // 解析响应
        SearchHits searchHits = response.getHits();
        // 获取总条数
        //long total = searchHits.getTotalHits().value;
        //文档数组
        SearchHit[] hits = searchHits.getHits();
        //遍历
        for (SearchHit hit : hits) {
            // 获取文档source
            String json = hit.getSourceAsString();
            // 反序列化
            Article article = JSON.parseObject(json,Article.class);
            res.add(article);
        }
        return res;
    }
}
