package com.semihshn.driverservice.adapter.elastic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semihshn.driverservice.domain.driver.Driver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ElasticListenerDriver {

    public final ElasticSearchAdapter elasticsearchService;
    public final ObjectMapper objectMapper;

    private static byte RETRY_COUNT = 0;

    public ElasticListenerDriver(ElasticSearchAdapter elasticsearchService, ObjectMapper objectMapper) {
        this.elasticsearchService = elasticsearchService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "driver-create-event", groupId = "driver-group")
    public void listenToDriverCreateEvent(String event) {
        try {
            RETRY_COUNT++;
            Driver driver = objectMapper.readValue(event, Driver.class);
            elasticsearchService.createIndex("drivers", driver.getId(), driver);
        } catch (Exception e) {
            if (RETRY_COUNT < 3) {
                listenToDriverCreateEvent(event);
            } else {
                log.info("Failed to save driver");
                e.printStackTrace();
            }
        }
    }

    @KafkaListener(topics = "driver-update-event", groupId = "driver-group")
    public void listenToDriverUpdateEvent(String event) {
        try {
            RETRY_COUNT++;
            Driver driver = objectMapper.readValue(event, Driver.class);
            elasticsearchService.update("drivers", driver.getId(), driver);
        } catch (Exception e) {
            if (RETRY_COUNT < 3) {
                listenToDriverCreateEvent(event);
            } else {
                log.info("Failed to update driver");
                e.printStackTrace();
            }
        }
    }

    @KafkaListener(topics = "driver-delete-event", groupId = "driver-group")
    public void listenToDriverDeleteEvent(String event) {
        try {
            RETRY_COUNT++;
            elasticsearchService.delete("drivers", event);
        } catch (Exception e) {
            if (RETRY_COUNT < 3) {
                listenToDriverDeleteEvent(event);
            } else {
                log.info("Failed to delete driver");
                e.printStackTrace();
            }
        }
    }
}