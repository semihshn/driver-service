package com.semihshn.driverservice.adapter.api.payment;

import com.google.gson.JsonElement;
import retrofit2.Call;
import retrofit2.http.*;

public interface PaymentRequest {
        @POST("/api/payments")
        Call<JsonElement> savePayment(@Body JsonElement requestBody);

        @DELETE("/api/payments/{paymentId}")
        Call<Void> deletePayment(@Path("paymentId") Long paymentId);

        @GET("/api/payments/{paymentId}")
        Call<JsonElement> getPaymentById(@Path("paymentId") Long paymentId);
}
