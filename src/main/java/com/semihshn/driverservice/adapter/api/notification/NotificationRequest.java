package com.semihshn.driverservice.adapter.api.notification;

import com.google.gson.JsonElement;
import retrofit2.Call;
import retrofit2.http.*;

public interface NotificationRequest {
        @POST("/api/notifications")
        Call<JsonElement> saveNotification(@Body JsonElement requestBody);

        @DELETE("/api/notifications/{notificationId}")
        Call<Void> deleteNotification(@Path("notificationId") Long notificationId);

        @GET("/api/notifications/{notificationId}")
        Call<JsonElement> getNotificationById(@Path("notificationId") Long notificationId);
}
