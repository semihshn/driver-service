package com.semihshn.driverservice.domain.port;

import java.io.IOException;
import java.util.List;

public interface ElasticSearchPort {

    <S> List<S> search(String indexName, Class<S> clazz) throws IOException;

    void delete(String index, String id);

    <S> S retrieveById(String indexName, Long id, Class<S> clazz) throws IOException;

    <S> List<S> retrieveByField(String indexName, String field, String searchText, Class<S> searchingObject) throws IOException;

    <S> void update(String indexName, Long id, S source) throws IOException;
}
