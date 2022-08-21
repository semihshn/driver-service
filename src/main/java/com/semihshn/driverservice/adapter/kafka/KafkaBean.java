package com.semihshn.driverservice.adapter.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaBean {

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> map = new HashMap<>();
        return new KafkaAdmin(map);
    }
    
    @Bean
    public NewTopic contactInfoTopic() {
        return new NewTopic("contact-info-events", 1, (short) 1);
    }

    @Bean
    public NewTopic driverTopic() {
        return new NewTopic("driver-events", 1, (short) 1);
    }


}
