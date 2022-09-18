package com.semihshn.driverservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EnableFeignClients
@PropertySource("classpath:application-${spring.profiles.active:default}.properties")
public class DriverServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriverServiceApplication.class, args);
    }

}
