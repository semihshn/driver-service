package com.semihshn.driverservice.adapter.api.notification;

import com.semihshn.driverservice.domain.api.Notification;
import com.semihshn.driverservice.domain.port.NotificationRestPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationApiAdapter implements NotificationRestPort {

    private final NotificationRequest notificationRequest;

    @Override
    public Object saveNotification(Notification notification) {
        return notificationRequest.saveNotification(notification);
    }

    @Override
    public void deleteNotification(Long notificationId) {

    }

    @Override
    public Object getNotificationById(Long notificationId) {
        return null;
    }
}
