package com.semihshn.driverservice.adapter.api.payment;

import com.semihshn.driverservice.domain.api.Payment;
import com.semihshn.driverservice.domain.port.PaymentPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentApiAdapter implements PaymentPort {

    private final PaymentRequest paymentServiceRequest;

    @Override
    public Object savePayment(Payment payment) {
        return paymentServiceRequest.savePayment(payment);
    }

    @Override
    public void deletePayment(Long paymentId) {

    }

    @Override
    public Object getPaymentById(Long paymentId) {
        return null;
    }
}
