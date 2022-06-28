package com.semihshn.driverservice.domain.port;

import com.semihshn.driverservice.domain.api.Payment;

public interface PaymentPort {

    Object savePayment(Payment payment);

    void deletePayment(Long paymentId);

    Object getPaymentById(Long paymentId);
}
