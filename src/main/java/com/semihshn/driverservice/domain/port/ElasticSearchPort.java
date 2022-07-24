package com.semihshn.driverservice.domain.port;

import java.io.IOException;
import java.util.List;

public interface ElasticSearchPort {

    <S> List<S> search(String indexName, Class<S> clazz) throws IOException;
}
