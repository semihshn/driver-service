package com.semihshn.driverservice.adapter.api.notification;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.semihshn.driverservice.adapter.api.retrofit.RetrofitUtil;
import com.semihshn.driverservice.domain.api.Notification;
import com.semihshn.driverservice.domain.api.Payment;
import com.semihshn.driverservice.domain.port.NotificationPort;
import com.semihshn.driverservice.domain.port.PaymentPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationApiAdapter implements NotificationPort {

    @Autowired
    private NotificationRequest notificationRequest;

    @Override
    public JsonElement saveNotification(Notification notification) {
        return RetrofitUtil.executeInBlock(notificationRequest.saveNotification(new Gson().toJsonTree(notification)));
    }

    @Override
    public void deleteNotification(Long notificationId) {

    }

    @Override
    public JsonElement getNotificationById(Long notificationId) {
        return null;
    }
}
