package com.semihshn.driverservice.adapter.rest.driver.command;

import com.semihshn.driverservice.DriverServiceApplicationTests;
import com.semihshn.driverservice.adapter.elastic.ElasticSearchService;
import com.semihshn.driverservice.adapter.jpa.driver.DriverEntity;
import com.semihshn.driverservice.adapter.jpa.driver.DriverJpaRepository;
import com.semihshn.driverservice.adapter.rest.driver.request.DriverCreateRequest;
import com.semihshn.driverservice.adapter.rest.driver.response.DriverCreateResponse;
import com.semihshn.driverservice.domain.driver.Driver;
import org.junit.ClassRule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@Import(com.semihshn.driverservice.adapter.kafka.KafkaProducer.class)
class DriverCommandControllerTest extends DriverServiceApplicationTests {

    @ClassRule
    public static DockerComposeContainer environment =
            new DockerComposeContainer(
                    new File("src/test/resources/test-compose.yml"))
                    .withLocalCompose(true)

                    .withExposedService("broker", 9092)
                    .withExposedService("elasticsearch", 9200)
                    .withExposedService("elasticsearch", 9300)
                    .withExposedService("postgresql", 5432)
                    .withExposedService("zookeeper", 2181)
                    .withExposedService("zookeeper", 2888)
                    .withExposedService("zookeeper", 3888)
                    .withExposedService("payment-service", 7777,
                            Wait.forLogMessage("started", 1))
                    .withExposedService("notification-service", 8888,
                            Wait.forLogMessage("started", 1));
    ;

    @Autowired
    DriverJpaRepository driverJpaRepository;

    @Autowired
    ElasticSearchService elasticSearchService;

//    @DynamicPropertySource
//    public static void properties(DynamicPropertyRegistry registry) {
//
//        String postgresUrl = environment.getServiceHost("postgres", POSTGRES_PORT)
//                + ":" +
//                environment.getServicePort("postgres", POSTGRES_PORT);
//
//        registry.add("spring.datasource.url", () -> "jdbc:postgresql://"+postgresUrl+"/eis");
//        registry.add("spring.datasource.username", () ->"postgres");
//        registry.add("spring.datasource.password", () ->"postgres");
//
//    }
//}


    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void create() throws IOException {

        //given
        DriverCreateRequest request = new DriverCreateRequest();
        request.setUserId(1L);
        request.setFirstName("Test_first_name");
        request.setLastName("Test_last_name");
        request.setBirthDate(LocalDate.now());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, BEARER_TOKEN);

        //when
        ResponseEntity<DriverCreateResponse> response = super.testRestTemplate.exchange("/api/drivers", HttpMethod.POST, new HttpEntity<>(request, httpHeaders), DriverCreateResponse.class);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Optional<DriverEntity> driverJpa = driverJpaRepository.findByUserId(1L);
        assertThat(driverJpa).isPresent();
        assertThat(driverJpa.get().getFirstName()).isEqualTo(request.getFirstName());

        List<Driver> driverElastic = elasticSearchService.retrieveByField("drivers", "firstName", request.getFirstName(), Driver.class);
        assertThat(driverElastic).isNotEmpty();
        assertThat(driverElastic.get(0).getFirstName()).isEqualTo(request.getFirstName());
    }

    @Test
    @Sql(scripts = "/driver-create.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_postgre() {
        //given
        Optional<DriverEntity> optionalDriver = driverJpaRepository.findById(1001L);
        assertThat(optionalDriver).isPresent();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, BEARER_TOKEN);

        //when
        testRestTemplate.exchange("/api/drivers/1001", HttpMethod.DELETE, new HttpEntity<>(httpHeaders), Void.class);

        //then
        optionalDriver = driverJpaRepository.findById(1001L);
        assertThat(optionalDriver).isEmpty();
    }
}