package com.semihshn.driverservice.domain.port;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ElasticSearchPort {

    <S> Optional<List<S>> search(String indexName, Class<S> clazz) throws IOException;
}
