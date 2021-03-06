package com.semihshn.driverservice.domain.port;

import com.semihshn.driverservice.domain.api.Notification;

public interface NotificationPort {
    Object saveNotification(Notification notification);

    void deleteNotification(Long notificationId);

    Object getNotificationById(Long notificationId);
}
