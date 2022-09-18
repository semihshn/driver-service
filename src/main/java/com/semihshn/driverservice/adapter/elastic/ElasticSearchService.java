package com.semihshn.driverservice.adapter.elastic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semihshn.driverservice.domain.port.ElasticSearchPort;
import com.semihshn.driverservice.domain.util.exception.ExceptionType;
import com.semihshn.driverservice.domain.util.exception.SemDataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class ElasticSearchService implements ElasticSearchPort {

    private final RestHighLevelClient restHighLevelClient;
    private final ObjectMapper mapper;

    public <T> void createIndex(String indexName, Long id, T source) throws IOException {
        String json = mapper.writeValueAsString(source);

        IndexRequest indexRequest = new IndexRequest(indexName);
        indexRequest.id(String.valueOf(id));
        indexRequest.source(json, XContentType.JSON);

        this.restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    }

    @Override
    public <S> List<S> search(String indexName, Class<S> searchingObject) throws IOException {

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        sourceBuilder.query(QueryBuilders.matchAllQuery());
        sourceBuilder.sort("id", SortOrder.DESC);
        sourceBuilder.from(0);
        sourceBuilder.size(2000);

        SearchRequest request = new SearchRequest(indexName);
        request.source(sourceBuilder);

        SearchResponse response = this.restHighLevelClient.search(request, RequestOptions.DEFAULT);

        return Stream.of(response.getHits().getHits())
                .map(hit -> readFromSearchHit(hit, searchingObject))
                .toList();
    }

    @Override
    public <S> List<S> retrieveByField(String indexName, String field, String searchText, Class<S> searchingObject) throws IOException {

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        sourceBuilder.query(QueryBuilders.termQuery(field, searchText));
        sourceBuilder.sort("id", SortOrder.DESC);


        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(sourceBuilder);

        SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        return Stream.of(response.getHits().getHits())
                .map(hit -> readFromSearchHit(hit, searchingObject))
                .toList();

    }


    private <S> S readFromSearchHit(SearchHit hit, Class<S> clazz) {
        try {
            return mapper.readValue(hit.getSourceAsString(), clazz);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    @Override
    public void delete(String indexName, String id) {

        DeleteRequest deleteRequest = new DeleteRequest(indexName);
        deleteRequest.id(id.toString());
        try {
            restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public <S> S retrieveById(String indexName, Long id, Class<S> clazz) throws IOException {

        GetRequest getRequest = new GetRequest(indexName, id.toString());

        GetResponse getResponse = null;

        try {
            getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            return mapper.convertValue(getResponse.getSource(), clazz);
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.NOT_FOUND) {
                throw new SemDataNotFoundException(ExceptionType.DRIVER_DATA_NOT_FOUND);
            }
        }

        return null;

    }

    @Override
    public <S> void update(String indexName, Long id, S source) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(indexName, id.toString());
        String jsonString = mapper.writeValueAsString(source);
        updateRequest.doc(jsonString, XContentType.JSON);

        restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
    }
}
