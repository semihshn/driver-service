package com.semihshn.driverservice.adapter.elastic;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ElasticClient {

    private final RestHighLevelClient elasticSearchClient;

    public ElasticClient(@Value("${elastic.search.domain}") String elasticSearchDomain) {
        this.elasticSearchClient = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(elasticSearchDomain, 9200, "http"),
                        new HttpHost(elasticSearchDomain, 9201, "http")
                ));
    }

    public RestHighLevelClient getElasticSearchClient() {
        return elasticSearchClient;
    }


}
