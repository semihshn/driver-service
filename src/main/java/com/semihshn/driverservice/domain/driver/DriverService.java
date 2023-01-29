package com.semihshn.driverservice.domain.driver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.semihshn.driverservice.domain.api.Payment;
import com.semihshn.driverservice.domain.port.*;
import com.semihshn.driverservice.domain.util.exception.ExceptionType;
import com.semihshn.driverservice.domain.util.exception.SemDataNotFoundException;
import com.semihshn.driverservice.domain.util.exception.SemKafkaException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverPersistencePort driverPort;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;
    private final ElasticSearchPort elasticSearchPort;
    private final DriverCachePort driverCachePort;

    private static final String driverIndexName = "drivers";

    public Driver create(Driver driver) {

        try {
            Payment paymentEvent = Payment.builder()
                    .userId(1L)
                    .cvv("225")
                    .expireDate("2020-08-22")
                    .cardType(Payment.CardType.CREDIT)
                    .ccNo("1123456894067408")
                    .amount(new BigInteger("12423"))
                    .build();

            kafkaTemplate.send("payment-create-event", mapper.writeValueAsString(paymentEvent));
        } catch (JsonProcessingException e) {
            throw new SemKafkaException(ExceptionType.KAFKA_ERROR, e.getMessage());
        }

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

        Optional<Driver> cacheDriver = driverCachePort.retrieveDriver(id);
        log.info("Driver is retrieving: {}", id);

        if (cacheDriver.isEmpty()) {
            log.info("Driver cache is updating: {}", id);
            Driver driver;

            try {
                driver = elasticSearchPort.retrieveById(driverIndexName, id, Driver.class);
                driverCachePort.createDriver(driver);
            } catch (RuntimeException | IOException e) {
                throw new SemDataNotFoundException(ExceptionType.DRIVER_DATA_NOT_FOUND, e.getMessage());
            }

            checkIfNull(driver);

            return driver;
        }

        return cacheDriver.get();

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
