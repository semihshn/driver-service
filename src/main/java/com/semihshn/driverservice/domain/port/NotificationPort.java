package com.semihshn.driverservice.domain.port;

import com.google.gson.JsonElement;
import com.semihshn.driverservice.domain.api.Notification;

public interface NotificationPort {
    JsonElement saveNotification(Notification notification);

    void deleteNotification(Long notificationId);

    JsonElement getNotificationById(Long notificationId);
}
