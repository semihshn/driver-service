package com.semihshn.driverservice.domain.driver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semihshn.driverservice.domain.api.Notification;
import com.semihshn.driverservice.domain.api.Payment;
import com.semihshn.driverservice.domain.port.DriverPort;
import com.semihshn.driverservice.domain.port.ElasticSearchPort;
import com.semihshn.driverservice.domain.port.NotificationPort;
import com.semihshn.driverservice.domain.port.PaymentPort;
import com.semihshn.driverservice.domain.util.exception.ExceptionType;
import com.semihshn.driverservice.domain.util.exception.SemDataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverPort driverPort;
    private final PaymentPort paymentPort;
    private final NotificationPort notificationPort;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;
    private final ElasticSearchPort elasticSearchPort;

    public Long create(Driver driver) {

        paymentPort.savePayment(
                Payment.builder()
                        .userId(1L)
                        .cvv("225")
                        .expireDate("2020-08-22")
                        .cardType(Payment.CardType.CREDIT)
                        .ccNo("1123456894067408")
                        .amount(new BigInteger("12423"))
                        .build()
        );

        notificationPort.saveNotification(
                Notification.builder()
                        .driverId(1L)
                        .firstName("Schumeer")
                        .lastName("Fernandes")
                        //.birthDate(LocalDate.now())
                        .telephoneAddress("542 234 23 53")
                        .message("Sürücü bilgileri kaydedildi, teşekkür ederiz")
                        .build()
        );

        Driver entity = driverPort.create(driver);

        try {
            kafkaTemplate.send("driver-events", mapper.writeValueAsString(entity));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return entity.getId();
    }

    public List<Driver> retrieveAll() throws IOException {
        return elasticSearchPort.search("drivers",Driver.class)
                .orElseThrow(() -> new SemDataNotFoundException(ExceptionType.DRIVER_DATA_NOT_FOUND));
    }

    public Driver retrieve(Long id) {
        return driverPort.retrieve(id);
    }

    public Driver retrieveByUserId(Long id) {
        return driverPort.retrieveByUserId(id);
    }

    public void delete(Long driverId) {
        driverPort.delete(driverId);
    }
}
