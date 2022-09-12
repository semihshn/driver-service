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
import com.semihshn.driverservice.domain.util.results.CommandResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import org.springframework.kafka.support.SendResult;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

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

        final Long[] driverId = new Long[1];

        final AtomicReference<CommandResponse<Object>> response = new AtomicReference<>(CommandResponse.ok(null));

        CompletableFuture<Void> future = CompletableFuture.supplyAsync(
                        () -> {
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

                            Driver temp = driverPort.create(driver);
                            driverId[0] =temp.getId();
                            return temp;
                        })
                .thenApply(entity -> {
                    try {
                        return kafkaTemplate.send("driver-events", mapper.writeValueAsString(entity));
                    } catch (JsonProcessingException e) {
                        response.set(new CommandResponse<>(null, false));
                        return response;
                    }
                })
                .thenAccept(call -> response.set(responseHandler(response)));

        future.join();
        return driverId[0];
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
