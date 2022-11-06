package com.semihshn.driverservice.domain.contactInfo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semihshn.driverservice.domain.driver.Driver;
import com.semihshn.driverservice.domain.port.ContactInfoCachePort;
import com.semihshn.driverservice.domain.port.ContactInfoPersistencePort;
import com.semihshn.driverservice.domain.port.DriverPersistencePort;
import com.semihshn.driverservice.domain.port.ElasticSearchPort;
import com.semihshn.driverservice.domain.util.exception.ExceptionType;
import com.semihshn.driverservice.domain.util.exception.SemDataNotFoundException;
import com.semihshn.driverservice.domain.util.exception.SemKafkaException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactInfoService {

    private final ContactInfoPersistencePort contactInformationPort;
    private final DriverPersistencePort driverPort;
    private final ObjectMapper mapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ElasticSearchPort elasticSearchPort;
    private final ContactInfoCachePort contactInfoCachePort;

    private static final String contactInfoIndexName = "contact-infos";

    public Long create(ContactInfo contactInformation) {

        Driver driver = driverPort.retrieve(contactInformation.getDriverId());

        ContactInfo entity = contactInformationPort.create(contactInformation, driver);

        try {
            kafkaTemplate.send("contact-info-create-event", mapper.writeValueAsString(entity));
        } catch (JsonProcessingException e) {
            throw new SemKafkaException(ExceptionType.KAFKA_ERROR, e.getMessage());
        }

        return entity.getId();
    }

    public List<ContactInfo> retrieveAll() throws IOException {
        List<ContactInfo> contactInfoList = elasticSearchPort.search(contactInfoIndexName, ContactInfo.class);

        checkIfEmpty(contactInfoList);

        return contactInfoList;
    }

    public ContactInfo retrieve(Long id) {

        Optional<ContactInfo> cacheContactInfo = contactInfoCachePort.retrieveContactInfo(id);
        log.info("Contact Info is retrieving: {}", id);

        if (cacheContactInfo.isEmpty()){
            log.info("Contact Info cache is updating: {}", id);
            ContactInfo retrievedContactInfo = null;

            try {
                retrievedContactInfo = elasticSearchPort.retrieveById(contactInfoIndexName, id, ContactInfo.class);
                contactInfoCachePort.createContactInfo(retrievedContactInfo);
            } catch (IOException e) {
                throw new SemDataNotFoundException(ExceptionType.CONTACT_INFO_DATA_NOT_FOUND, e.getMessage());
            }

            checkIfNull(retrievedContactInfo);

            return retrievedContactInfo;
        }

        return cacheContactInfo.get();
    }

    public void delete(Long id) {
        retrieve(id);
        contactInformationPort.delete(id);
        kafkaTemplate.send("contact-info-delete-event", id.toString());
    }

    public Long update(ContactInfo contactInfo) throws IOException {
        retrieve(contactInfo.getId());

        Driver driver = driverPort.retrieve(contactInfo.getDriverId());
        ContactInfo updatedContactInfo = contactInformationPort.update(contactInfo, driver);
        try {
            kafkaTemplate.send("contact-info-update-event", mapper.writeValueAsString(contactInfo));
        } catch (JsonProcessingException e) {
            throw new SemKafkaException(ExceptionType.KAFKA_ERROR, e.getMessage());
        }

        return updatedContactInfo.getId();
    }

    public List<ContactInfo> retrieveByDriverId(Long driverId) throws IOException {
        List<ContactInfo> contactInfoList = elasticSearchPort.retrieveByField(contactInfoIndexName, "driverId", driverId.toString(), ContactInfo.class);

        checkIfEmpty(contactInfoList);

        return contactInfoList;
    }

    private void checkIfEmpty(List<ContactInfo> contactInfoList) {
        if (contactInfoList.isEmpty()) {
            throw new SemDataNotFoundException(ExceptionType.CONTACT_INFO_DATA_NOT_FOUND, "İletişim bilgisi bulunamadı.");
        }
    }

    private void checkIfNull(ContactInfo contactInfo) {
        if (contactInfo == null) {
            throw new SemDataNotFoundException(ExceptionType.CONTACT_INFO_DATA_NOT_FOUND, "İletişim bilgisi bulunamadı.");
        }
    }
}
