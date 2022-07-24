package com.semihshn.driverservice.domain.contactInfo;

import com.semihshn.driverservice.domain.cqrsUtil.CommandResponse;
import com.semihshn.driverservice.domain.driver.Driver;
import com.semihshn.driverservice.domain.port.ContactInfoPort;
import com.semihshn.driverservice.domain.port.DriverPort;
import com.semihshn.driverservice.domain.port.ElasticSearchPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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

        final Long[] contactInfoId = new Long[1];

        final AtomicReference<CommandResponse<Object>> response = new AtomicReference<>(CommandResponse.ok(null));

        CompletableFuture<Void> future = CompletableFuture.supplyAsync(
                        () -> {
                            Driver driver = driverPort.retrieve(contactInformation.getDriverId());
                            ContactInfo temp = contactInformationPort.create(contactInformation, driver);
                            contactInfoId[0] =temp.getId();
                            return temp;
                        })
                .thenApply(entity -> {
                    try {
                        return kafkaTemplate.send("contact-info-events", mapper.writeValueAsString(entity));
                    } catch (JsonProcessingException e) {
                        response.set(new CommandResponse<>(null, false));
                        return response;
                    }
                })
                .thenAccept(call -> response.set(responseHandler(response)));

        future.join();
        return contactInfoId[0];
    }

    public List<ContactInfo> retrieveAll() throws IOException {
        return elasticSearchPort.search("contact-infos",ContactInfo.class);
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
