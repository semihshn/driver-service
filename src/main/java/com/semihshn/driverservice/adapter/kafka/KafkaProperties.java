package com.semihshn.driverservice.adapter.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaProperties {

    @NotBlank
    private String bootstrapServers;

    @NotBlank
    private String groupId;
}
