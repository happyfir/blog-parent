package org.happyfire.blog.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("org.happyfire.blog.dao.mapper")
public class elasticsearchConfig {
    @Bean
    public RestHighLevelClient client(){
        return new RestHighLevelClient(RestClient.builder(HttpHost.create("http://192.168.180.131:9200")));
    }
}
