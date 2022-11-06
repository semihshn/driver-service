package com.semihshn.driverservice.adapter.redis;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {

    @NotBlank
    private String host;

    @NotNull
    private Integer port;
}
