package com.semihshn.driverservice.domain.driver;

import com.semihshn.driverservice.domain.api.Notification;
import com.semihshn.driverservice.domain.api.Payment;
import com.semihshn.driverservice.domain.port.DriverPort;
import com.semihshn.driverservice.domain.port.NotificationPort;
import com.semihshn.driverservice.domain.port.PaymentPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverPort driverPort;
    private final PaymentPort paymentPort;
    private final NotificationPort notificationPort;

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

        Driver temp = driverPort.create(driver);
        return temp.getId();
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
