package com.semihshn.driverservice.adapter.elastic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semihshn.driverservice.domain.contactInfo.ContactInfo;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public record ElasticListener(ElasticSearchService elasticsearchService,
                              ObjectMapper objectMapper) {

    private static byte RETRY_COUNT = 0;

    private static final Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(ElasticListener.class);

    @KafkaListener(topics = "contact-info-events", groupId = "driver-group-id")
    public void listen(String event) {
        try {
            RETRY_COUNT++;
            ContactInfo contactInfo = objectMapper.readValue(event, ContactInfo.class);
            elasticsearchService.createIndex("contact-infos", contactInfo.getId(), contactInfo);
        } catch (Exception e) {
            if (RETRY_COUNT < 3) {
                listen(event);
            } else {
                LOGGER.info("Failed to save contact info of driver");
                e.printStackTrace();
            }
        }
    }
}