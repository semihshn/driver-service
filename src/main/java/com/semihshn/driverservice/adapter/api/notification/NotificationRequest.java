package com.semihshn.driverservice.adapter.api.notification;

import com.semihshn.driverservice.adapter.api.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "notification-service"//Name of notification-service application
        , path = "/api/notifications"//Pre-path for notification-service
        , url = "${notification.service.url}"
        , configuration = FeignConfiguration.class
)
public interface NotificationRequest {

        @PostMapping
        Object saveNotification(@RequestBody Object requestBody);

        @DeleteMapping("{notificationId}")
        void deleteNotification(@PathVariable("notificationId") Long notificationId);

        @GetMapping("{notificationId}")
        Object getNotificationById(@PathVariable("notificationId") Long notificationId);
}
