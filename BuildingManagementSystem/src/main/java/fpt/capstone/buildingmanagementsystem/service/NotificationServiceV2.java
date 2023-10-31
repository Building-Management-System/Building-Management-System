package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.model.entity.Notification;
import fpt.capstone.buildingmanagementsystem.model.response.NotificationResponse;
import fpt.capstone.buildingmanagementsystem.model.response.NotificationTitleResponse;
import fpt.capstone.buildingmanagementsystem.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NotificationServiceV2 {

    @Autowired
    private NotificationRepository notificationRepository;

    public NotificationTitleResponse getAllNotificationByUser(String userId) {

        List<NotificationResponse> notifications = notificationRepository.getNotificationByUserId(userId)
                .stream().map(notification -> new NotificationResponse(
                        notification.getNotificationId(),
                        notification.getTitle(),
                        notification.getUploadDate(),
                        true
                )).collect(Collectors.toList());

        Map<String, Notification> unreadNotification = notificationRepository.getUnreadMarkNotificationByUserId(userId)
                .stream().collect(Collectors.toMap(Notification::getNotificationId, Function.identity()));

        notifications.forEach(response -> {
            if (unreadNotification.containsKey(response.getNotificationId())) response.setReadStatus(false);
        });
        return new NotificationTitleResponse(notifications.size(), notifications);
    }
}
