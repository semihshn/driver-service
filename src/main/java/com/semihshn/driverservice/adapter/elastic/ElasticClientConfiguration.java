package com.semihshn.driverservice.adapter.elastic;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Data
@Component
@Slf4j
public class ElasticClientConfiguration {

    @Value("${elastic.search.domain}")
    private String elasticSearchDomain;

    @Bean
    public RestHighLevelClient setElasticSearchClient() {
        log.info(String.format("elasticsearch host: %s", elasticSearchDomain));

        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(elasticSearchDomain, 9200, "http"),
                        new HttpHost(elasticSearchDomain, 9201, "http")
                ));
    }

}
