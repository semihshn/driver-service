package com.semihshn.driverservice.integration.driver;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestPropertySource;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features.driver",
        plugin = {"pretty", "html:target/cucumber/driver-features.html"},
        glue = {"com.semihshn.driverservice.integration"})
@TestPropertySource(locations="classpath:application-test.properties")
public class DriverIntegrationTest {

    protected String BEARER_TOKEN = "Basic cmFuZG9tU2VjdXJlS2V5VXNlcm5hbWUhOnJhbmRvbVNlY3VyZUtleVBhc3N3b3JkIQ==";

    @Autowired
    protected TestRestTemplate testRestTemplate;
}
