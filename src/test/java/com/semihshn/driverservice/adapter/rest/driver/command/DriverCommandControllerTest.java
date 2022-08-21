package com.semihshn.driverservice.adapter.rest.driver.command;

import com.semihshn.driverservice.DriverServiceApplicationTests;
import com.semihshn.driverservice.adapter.jpa.driver.DriverEntity;
import com.semihshn.driverservice.adapter.jpa.driver.DriverJpaAdapter;
import com.semihshn.driverservice.adapter.jpa.driver.DriverJpaRepository;
import com.semihshn.driverservice.adapter.rest.driver.request.DriverCreateRequest;
import com.semihshn.driverservice.adapter.rest.driver.response.DriverCreateResponse;
import com.semihshn.driverservice.domain.port.DriverPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class DriverCommandControllerTest extends DriverServiceApplicationTests {
    @Autowired
    DriverJpaRepository driverJpaRepository;

    @Test
    @Sql(scripts = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void create() {
        //given
        DriverCreateRequest request = new DriverCreateRequest();
        request.setUserId(Long.valueOf(1));
        request.setFirstName("Test_first_name");
        request.setLastName("Test_last_name");
        request.setBirhDate(LocalDate.now());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set(HttpHeaders.AUTHORIZATION, BEARER_TOKEN);

        //when
        ResponseEntity<DriverCreateResponse> response = testRestTemplate.exchange("/api/drivers", HttpMethod.POST, new HttpEntity<>(request, httpHeaders), DriverCreateResponse.class);

        //then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        Optional<DriverEntity> driver = driverJpaRepository.findByUserId(Long.valueOf(1));
        assertThat(driver).isPresent();
        assertThat(driver.get().getFirstName()).isEqualTo("Test_first_name");
    }

    @Test
    void delete() {
    }
}