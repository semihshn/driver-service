package com.semihshn.driverservice.domain.contactInfo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semihshn.driverservice.domain.driver.Driver;
import com.semihshn.driverservice.domain.port.ContactInfoPort;
import com.semihshn.driverservice.domain.port.DriverPort;
import com.semihshn.driverservice.domain.port.ElasticSearchPort;
import com.semihshn.driverservice.domain.util.exception.ExceptionType;
import com.semihshn.driverservice.domain.util.exception.SemDataNotFoundException;
import com.semihshn.driverservice.domain.util.results.CommandResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class ContactInfoService {

    private final ContactInfoPort contactInformationPort;
    private final DriverPort driverPort;
    private final ObjectMapper mapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ElasticSearchPort elasticSearchPort;

    public Long create(ContactInfo contactInformation) {

        Driver driver = driverPort.retrieve(contactInformation.getDriverId());
        ContactInfo entity = contactInformationPort.create(contactInformation, driver);

        try {
            kafkaTemplate.send("contact-info-events", mapper.writeValueAsString(entity));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

       return entity.getId();
    }

    public List<ContactInfo> retrieveAll() throws IOException {
            return elasticSearchPort.search("contact-infos",ContactInfo.class)
                    .orElseThrow(() -> new SemDataNotFoundException(ExceptionType.CONTACT_INFO_DATA_NOT_FOUND));
    }

    public ContactInfo retrieve(Long id) {
        return contactInformationPort.retrieve(id);
    }

    public void delete(Long id) {
        contactInformationPort.delete(id);
    }


    private CommandResponse<Object> responseHandler(AtomicReference<CommandResponse<Object>> o) {
        if (o.get().getResponse() != null) {
            @SuppressWarnings("unchecked")
            ListenableFuture<SendResult<?,?>> result = (ListenableFuture<SendResult<?,?>>) o.get().getResponse();
            o.set(CommandResponse.ok(result));
            return o.get();
        }
        o.set(CommandResponse.error(null));
        return o.get();
    }
}
