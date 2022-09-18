package com.semihshn.driverservice.domain.contactInfo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semihshn.driverservice.domain.driver.Driver;
import com.semihshn.driverservice.domain.port.ContactInfoPort;
import com.semihshn.driverservice.domain.port.DriverPort;
import com.semihshn.driverservice.domain.port.ElasticSearchPort;
import com.semihshn.driverservice.domain.util.exception.ExceptionType;
import com.semihshn.driverservice.domain.util.exception.SemDataNotFoundException;
import com.semihshn.driverservice.domain.util.exception.SemKafkaException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactInfoService {

    private final ContactInfoPort contactInformationPort;
    private final DriverPort driverPort;
    private final ObjectMapper mapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ElasticSearchPort elasticSearchPort;

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

        ContactInfo contactInfo = null;

        try {
            contactInfo = elasticSearchPort.retrieveById(contactInfoIndexName, id, ContactInfo.class);
        } catch (IOException e) {
            throw new SemDataNotFoundException(ExceptionType.CONTACT_INFO_DATA_NOT_FOUND, e.getMessage());
        }

        checkIfNull(contactInfo);

        return contactInfo;
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
