package com.semihshn.driverservice.domain.port;

import com.google.gson.JsonElement;
import com.semihshn.driverservice.domain.api.Payment;

public interface PaymentPort {

    JsonElement savePayment(Payment payment);

    void deletePayment(Long paymentId);

    JsonElement getPaymentById(Long paymentId);
}
