package com.semihshn.driverservice.adapter.api.payment;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.semihshn.driverservice.adapter.api.retrofit.RetrofitUtil;
import com.semihshn.driverservice.domain.api.Payment;
import com.semihshn.driverservice.domain.port.PaymentPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentApiAdapter implements PaymentPort {

    @Autowired
    private PaymentRequest paymentServiceRequest;

    @Override
    public JsonElement savePayment(Payment payment) {
        return RetrofitUtil.executeInBlock(paymentServiceRequest.savePayment(new Gson().toJsonTree(payment)));
    }

    @Override
    public void deletePayment(Long paymentId) {

    }

    @Override
    public JsonElement getPaymentById(Long paymentId) {
        return null;
    }
}
