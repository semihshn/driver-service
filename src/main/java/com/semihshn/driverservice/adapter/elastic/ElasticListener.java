package com.semihshn.driverservice.adapter.elastic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semihshn.driverservice.domain.contactInfo.ContactInfo;
import com.semihshn.driverservice.domain.driver.Driver;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public record ElasticListener(ElasticSearchService elasticsearchService,
                              ObjectMapper objectMapper) {

    private static byte RETRY_COUNT = 0;

    private static final Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(ElasticListener.class);

    @KafkaListener(topics = "contact-info-events", groupId = "driver-group-id")
    public void listenToContactInfo(String event) {
        try {
            RETRY_COUNT++;
            ContactInfo contactInfo = objectMapper.readValue(event, ContactInfo.class);
            elasticsearchService.createIndex("contact-infos", contactInfo.getId(), contactInfo);
        } catch (Exception e) {
            if (RETRY_COUNT < 3) {
                listenToContactInfo(event);
            } else {
                LOGGER.info("Failed to save contact info of driver");
                e.printStackTrace();
            }
        }
    }

    @KafkaListener(topics = "driver-events", groupId = "driver-group-id")
    public void listenToDriver(String event) {
        try {
            RETRY_COUNT++;
            Driver driver = objectMapper.readValue(event, Driver.class);
            elasticsearchService.createIndex("drivers", driver.getId(), driver);
        } catch (Exception e) {
            if (RETRY_COUNT < 3) {
                listenToDriver(event);
            } else {
                LOGGER.info("Failed to save driver");
                e.printStackTrace();
            }
        }
    }
}