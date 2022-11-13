package com.semihshn.driverservice.adapter.elastic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semihshn.driverservice.domain.contactInfo.ContactInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ElasticListenerContactInfo {

    public final ElasticSearchAdapter elasticsearchService;
    public final ObjectMapper objectMapper;

    private static byte RETRY_COUNT = 0;

    public ElasticListenerContactInfo(ElasticSearchAdapter elasticsearchService, ObjectMapper objectMapper) {
        this.elasticsearchService = elasticsearchService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "contact-info-create-event", groupId = "driver-group")
    public void listenToContactInfoCreateEvent(String event) {
        try {
            RETRY_COUNT++;
            ContactInfo contactInfo = objectMapper.readValue(event, ContactInfo.class);
            elasticsearchService.createIndex("contact-infos", contactInfo.getId(), contactInfo);
        } catch (Exception e) {
            if (RETRY_COUNT < 3) {
                listenToContactInfoCreateEvent(event);
            } else {
                log.info("Failed to save contact info of driver");
                e.printStackTrace();
            }
        }
    }

    @KafkaListener(topics = "contact-info-update-event", groupId = "driver-group")
    public void listenToContactInfoUpdateEvent(String event) {
        try {
            RETRY_COUNT++;
            ContactInfo contactInfo = objectMapper.readValue(event, ContactInfo.class);
            elasticsearchService.update("contact-infos", contactInfo.getId(), contactInfo);
        } catch (Exception e) {
            if (RETRY_COUNT < 3) {
                listenToContactInfoUpdateEvent(event);
            } else {
                log.info("Failed to update contact info of driver");
                e.printStackTrace();
            }
        }
    }

    @KafkaListener(topics = "contact-info-delete-event", groupId = "driver-group")
    public void listenToContactInfoDeleteEvent(String event) {
        try {
            RETRY_COUNT++;
            elasticsearchService.delete("contact-infos", event);
        } catch (Exception e) {
            if (RETRY_COUNT < 3) {
                listenToContactInfoDeleteEvent(event);
            } else {
                log.info("Failed to delete contact info of driver");
                e.printStackTrace();
            }
        }
    }
}