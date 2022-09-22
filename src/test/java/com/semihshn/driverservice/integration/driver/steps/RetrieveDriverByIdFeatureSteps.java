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
import org.junit.ClassRule;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@Testcontainers
@RequiredArgsConstructor
public class RetrieveDriverByIdFeatureSteps extends DriverIntegrationTest {

    @ClassRule
    public static DockerComposeContainer environment =
            new DockerComposeContainer(new File("src/test/resources/test-compose.yml"))
                    .withExposedService("notification-service", 8888)
                    .withExposedService("payment-service", 7777)
                    .withExposedService("broker", 9092)
                    .withExposedService("elasticsearch", 9200)
                    .withExposedService("elasticsearch", 9300);


    @LocalServerPort
    private int port;

    private final ElasticSearchService elasticSearchService;
    HttpHeaders httpHeaders = new HttpHeaders();

    private Long driverId;
    private ResponseEntity<DriverResponse> driverResponseResponseEntity = null;

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

    @Then("^client receives status code (\\d+)$")
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
