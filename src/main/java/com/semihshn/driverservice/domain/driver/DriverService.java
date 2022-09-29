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
import com.semihshn.driverservice.domain.util.exception.SemKafkaException;
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

    private static final String driverIndexName = "drivers";

    public Driver create(Driver driver) {

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
            kafkaTemplate.send("driver-create-event", mapper.writeValueAsString(entity));
        } catch (JsonProcessingException e) {
            throw new SemKafkaException(ExceptionType.KAFKA_ERROR, e.getMessage());
        }

        return entity;
    }

    public List<Driver> retrieveAll() throws IOException {

        List<Driver> driverList;

        try {
            driverList = elasticSearchPort.search(driverIndexName, Driver.class);
        } catch (RuntimeException e) {
            throw new SemDataNotFoundException(ExceptionType.DRIVER_DATA_NOT_FOUND, e.getMessage());
        }

        checkIfEmpty(driverList);

        return driverList;
    }

    public Driver retrieve(Long id) {

        Driver driver;

        try {
            driver = elasticSearchPort.retrieveById(driverIndexName, id, Driver.class);
        } catch (RuntimeException | IOException e) {
            throw new SemDataNotFoundException(ExceptionType.DRIVER_DATA_NOT_FOUND, e.getMessage());
        }

        checkIfNull(driver);

        return driver;
    }

    public List<Driver> retrieveByUserId(Long id) throws IOException {

        List<Driver> driverList;

        try {
            driverList = elasticSearchPort.retrieveByField(driverIndexName, "userId", id.toString(), Driver.class);
        } catch (RuntimeException e) {
            throw new SemDataNotFoundException(ExceptionType.DRIVER_DATA_NOT_FOUND, e.getMessage());
        }

        checkIfEmpty(driverList);

        return driverList;
    }

    public Driver update(Driver driver) throws IOException {
        retrieve(driver.getId());

        Driver updatedDriver = driverPort.update(driver);
        try {
            kafkaTemplate.send("driver-update-event", mapper.writeValueAsString(driver));
        } catch (JsonProcessingException e) {
            throw new SemKafkaException(ExceptionType.KAFKA_ERROR, e.getMessage());
        }

        return updatedDriver;
    }

    public void delete(Long driverId) {
        retrieve(driverId);
        driverPort.delete(driverId);
        kafkaTemplate.send("driver-delete-event", driverId.toString());
    }

    private void checkIfEmpty(List<Driver> driverList) {
        if (driverList.isEmpty()) {
            throw new SemDataNotFoundException(ExceptionType.DRIVER_DATA_NOT_FOUND, "Herhangi bir kayıt bulunamadı.");
        }
    }

    private void checkIfNull(Driver driver) {
        if (driver == null) {
            throw new SemDataNotFoundException(ExceptionType.DRIVER_DATA_NOT_FOUND, "Herhangi bir kayıt bulunamadı.");
        }
    }
}
