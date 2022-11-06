package com.semihshn.driverservice.adapter.elastic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semihshn.driverservice.domain.contactInfo.ContactInfo;
import com.semihshn.driverservice.domain.driver.Driver;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ElasticListener {

    public final ElasticSearchAdapter elasticsearchService;
    public final ObjectMapper objectMapper;

    private static byte RETRY_COUNT = 0;

    private static final Logger LOGGER = org.apache.logging.log4j.LogManager.getLogger(ElasticListener.class);

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
                LOGGER.info("Failed to save contact info of driver");
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
                listenToDriverCreateEvent(event);
            } else {
                LOGGER.info("Failed to update contact info of driver");
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
                listenToDriverDeleteEvent(event);
            } else {
                LOGGER.info("Failed to delete contact info of driver");
                e.printStackTrace();
            }
        }
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
                LOGGER.info("Failed to save driver");
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
                LOGGER.info("Failed to update driver");
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
                LOGGER.info("Failed to delete driver");
                e.printStackTrace();
            }
        }
    }
}