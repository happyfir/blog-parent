package org.happyfire.blog.common.elasticsearch;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.happyfire.blog.dao.pojo.Article;
import org.happyfire.blog.service.ArticleService;
import org.happyfire.blog.service.impl.ArticleServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.happyfire.blog.constants.BlogArticleConstants.Article_Mapping;
import static org.happyfire.blog.constants.BlogArticleConstants.Create_Mapping;

/**
 * 用于创建ES索引库 导入数据 更新等初始化操作
 */
@SpringBootTest
public class CreatES {
    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private RestClientService restClientService;

    @Test
    void creatBlogIndex(){
//        List<Article> articleList = restClientService.search("苏打");
//        System.out.println(articleList);
        System.out.println(client);
    }

    /**
     * 创建文章索引库
     */
    @Test
    void createBlogArticleIndex(){
        //1、创建Resquest对象
        CreateIndexRequest request = new CreateIndexRequest(Article_Mapping);
        //2、准备请求的参数：DSL语句
        request.source(Create_Mapping, XContentType.JSON);
        //3、发送请求
        try {
            client.indices().create(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 批量导入数据
     */
    @Test
    void BulikRequest() throws IOException {
        //批量查询文章数据
        List<Article> articles = articleService.listArticles();
        // 1.创建Request
        BulkRequest request = new BulkRequest();
        // 2.准备参数，添加多个新增的Request
        for (Article article : articles) {
            // 2.2.创建新增文档的Request对象
            request.add(new IndexRequest(Article_Mapping)
                    .id(article.getId().toString())
                    .source(JSON.toJSONString(article), XContentType.JSON));
        }
        // 3.发送请求
        client.bulk(request, RequestOptions.DEFAULT);
    }


//    @BeforeEach
//    void setUp() {
//        this.client = new RestHighLevelClient(RestClient.builder(
//                HttpHost.create("http://192.168.180.131:9200")
//        ));
//    }
//
//    @AfterEach
//    void tearDown() throws IOException {
//        this.client.close();
//    }
}
