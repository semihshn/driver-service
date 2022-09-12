package com.semihshn.driverservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DriverServiceApplicationTests {

    protected String BEARER_TOKEN = "Basic cmFuZG9tU2VjdXJlS2V5VXNlcm5hbWUhOnJhbmRvbVNlY3VyZUtleVBhc3N3b3JkIQ==";

    @LocalServerPort
    protected int serverPort;

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Test
    void contextLoads() {
    }

}
