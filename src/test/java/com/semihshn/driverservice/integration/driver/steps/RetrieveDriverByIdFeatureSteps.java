package com.semihshn.driverservice.integration.driver.steps;

import com.semihshn.driverservice.adapter.elastic.ElasticSearchService;
import com.semihshn.driverservice.adapter.rest.driver.response.DriverResponse;
import com.semihshn.driverservice.domain.driver.Driver;
import com.semihshn.driverservice.integration.driver.DriverIntegrationTest;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.startupcheck.MinimumDurationRunningStartupCheckStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@Testcontainers
@RequiredArgsConstructor
public class RetrieveDriverByIdFeatureSteps extends DriverIntegrationTest {

    @LocalServerPort
    private int port;

    private final ElasticSearchService elasticSearchService;
    HttpHeaders httpHeaders = new HttpHeaders();

    private Long driverId;
    private ResponseEntity<DriverResponse> driverResponseResponseEntity = null;


    Network network = Network.newNetwork();

    @Container
    public GenericContainer elasticContainer = new GenericContainer(DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:7.8.0"))
            .withExposedPorts(9200, 9300)
            .withEnv("discovery.type", "single-node")
            .withEnv("max_open_files", "65536")
            .withEnv("max_content_length_in_bytes", "100000000")
            .withEnv("transport.host", "elasticsearch")
            .withNetwork(network)
            .withStartupCheckStrategy(
                    new MinimumDurationRunningStartupCheckStrategy(Duration.ofSeconds(1))
            );

    @Container
    public GenericContainer zookeperContainer = new GenericContainer(DockerImageName.parse("confluentinc/cp-zookeeper:7.0.1"))
            .withExposedPorts(2181, 2888, 3888)
            .withEnv("ZOOKEEPER_CLIENT_PORT", "2181")
            .withEnv("ZOOKEEPER_TICK_TIME", "2000")
            .withNetwork(network);

    @Container
    public GenericContainer kafkaContainer = new GenericContainer(DockerImageName.parse("confluentinc/cp-kafka:7.0.1"))
            .withExposedPorts(9092)
            .dependsOn(zookeperContainer)
            .withEnv("KAFKA_BROKER_ID", "1")
            .withEnv("KAFKA_ZOOKEEPER_CONNECT", "zookeeper:2181")
            .withEnv("KAFKA_LISTENER_SECURITY_PROTOCOL_MAP", "PLAINTEXT:PLAINTEXT,PLAINTEXT_INTERNAL:PLAINTEXT")
            .withEnv("KAFKA_ADVERTISED_LISTENERS", "PLAINTEXT://localhost:9092,PLAINTEXT_INTERNAL://broker:29092")
            .withEnv("KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR", "1")
            .withEnv("KAFKA_TRANSACTION_STATE_LOG_MIN_ISR", "1")
            .withEnv("KAFKA_TRANSACTION_STATE_LOG_REPLICATION_FACTOR", "1")
            .withEnv("GROUP_ID", "driver-group-id")
            .withEnv("KAFKA_CREATE_TOPICS", "contact-info-events")
            .withNetwork(network);


    @Given("^db contains an driver with valid id$")
    public void create_driver_with() throws IOException {
        Driver driver = Driver.builder()
                .id(1L)
                .userId(1L)
                .firstName("semihshn")
                .lastName("shn")
                .birthDate(LocalDate.now())
                .build();

        elasticSearchService.createIndex("drivers", driver.getId(), driver);

        driverId = driver.getId();
    }

    @And("^set bearer token$")
    public void set_bearer_token() {
        httpHeaders.set(HttpHeaders.AUTHORIZATION, BEARER_TOKEN);
    }

    @When("^client makes a call to GET '(.*)' with id$")
    public void call_endpoint(String endpoint) {
        driverResponseResponseEntity = super.testRestTemplate.exchange(
                String.format("http://localhost:%1$d/%2$s/%3$s", port, endpoint, driverId),
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                DriverResponse.class);
    }

    @Then("^client receives status code (\\d+)")
    public void check_status_code(int statusCode) {
        assertEquals(statusCode, driverResponseResponseEntity.getStatusCodeValue());
    }

    @And("^client id is valid$")
    public void check_driver_id() {
        assertEquals(driverId, driverResponseResponseEntity.getBody().getId());
    }

    @When("^client makes a call to GET '(.*)' with invalid id$")
    public void call_endpoint_invalid_id(String endpoint) {
        Long driverId = 1001L;
        driverResponseResponseEntity = super.testRestTemplate.exchange(
                String.format("http://localhost:%1$d/%2$s/%3$s", port, endpoint, driverId),
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                DriverResponse.class);
    }
}
