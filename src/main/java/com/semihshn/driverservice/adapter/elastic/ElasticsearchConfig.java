package com.semihshn.driverservice.adapter.elastic;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ElasticsearchConfig {

    private final ElasticsearchProperties elasticsearchProperties;

    public ElasticsearchConfig(ElasticsearchProperties elasticsearchProperties) {
        this.elasticsearchProperties = elasticsearchProperties;
    }

    @Bean
    public RestHighLevelClient setElasticSearchClient() {
        log.info(String.format("elasticsearch host: %s", elasticsearchProperties.getHost()));

        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(elasticsearchProperties.getHost(), 9200, "http"),
                        new HttpHost(elasticsearchProperties.getHost(), 9201, "http")
                ));
    }

}
