package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.model.entity.Notification;
import fpt.capstone.buildingmanagementsystem.model.entity.NotificationFile;
import fpt.capstone.buildingmanagementsystem.model.entity.NotificationImage;
import fpt.capstone.buildingmanagementsystem.model.response.NotificationDetailResponse;
import fpt.capstone.buildingmanagementsystem.model.response.NotificationResponse;
import fpt.capstone.buildingmanagementsystem.model.response.NotificationTitleResponse;
import fpt.capstone.buildingmanagementsystem.repository.NotificationFileRepository;
import fpt.capstone.buildingmanagementsystem.repository.NotificationImageRepository;
import fpt.capstone.buildingmanagementsystem.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NotificationServiceV2 {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationFileRepository notificationFileRepository;

    @Autowired
    private NotificationImageRepository notificationImageRepository;

    public NotificationTitleResponse getAllNotificationByUser(String userId) {

        Map<String, Notification> hiddenNotification = notificationRepository.getHiddenNotificationByUserId(userId)
                .stream()
                .collect(Collectors.toMap(Notification::getNotificationId, Function.identity()));

        List<NotificationResponse> notifications = notificationRepository.getNotificationByUserId(userId)
                .stream()
                .filter(notification -> !hiddenNotification.containsKey(notification.getNotificationId()))
                .map(notification -> new NotificationResponse(
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

    public List<NotificationDetailResponse> getListNotificationByUserId(String userId) {

        Map<String, Notification> hiddenNotification = notificationRepository.getHiddenNotificationByUserId(userId)
                .stream()
                .collect(Collectors.toMap(Notification::getNotificationId, Function.identity()));

        List<Notification> notifications = notificationRepository.getNotificationByUserId(userId)
                .stream().filter(notification -> !hiddenNotification.containsKey(notification.getNotificationId()))
                .collect(Collectors.toList());

        Map<String, NotificationFile> files = notificationFileRepository.findByNotificationIn(notifications)
                .stream().collect(Collectors.toMap(file -> file.getNotification().getNotificationId(), Function.identity()));

        Map<String, NotificationImage> images = notificationImageRepository.findByNotificationIn(notifications)
                .stream().collect(Collectors.toMap(image -> image.getNotification().getNotificationId(), Function.identity()));

        Map<String, Notification> personalPriorities = notificationRepository.getPersonalPriorityByUserId(userId)
                .stream().collect(Collectors.toMap(Notification::getNotificationId, Function.identity()));

        List<NotificationDetailResponse> notificationDetailResponses = new ArrayList<>();

        notifications.forEach(notification -> {
            NotificationDetailResponse detailResponse = new NotificationDetailResponse();
            detailResponse.setNotificationId(notification.getNotificationId());
            detailResponse.setTitle(notification.getTitle());
            detailResponse.setContent(notification.getContent());
            detailResponse.setUploadDate(notification.getUploadDate());
            detailResponse.setNotificationStatus(notification.getNotificationStatus());
            detailResponse.setPriority(notification.isPriority());
            detailResponse.setCreatorId(notification.getCreatedBy().getUserId());
            detailResponse.setCreatorFirstName(notification.getCreatedBy().getFirstName());
            detailResponse.setCreatorLastName(notification.getCreatedBy().getLastName());
            detailResponse.setReadStatus(true);

            notificationDetailResponses.add(detailResponse);
        });

        Map<String, Notification> unreadNotification = notificationRepository.getUnreadMarkNotificationByUserId(userId)
                .stream().collect(Collectors.toMap(Notification::getNotificationId, Function.identity()));

        notificationDetailResponses.forEach(response -> {
            if (unreadNotification.containsKey(response.getNotificationId())) {
                response.setReadStatus(false);
            }
            if (files.containsKey(response.getNotificationId())) {
                response.setFileId(files.get(response.getNotificationId()).getFileId());
                response.setFileName(files.get(response.getNotificationId()).getName());
                response.setType(files.get(response.getNotificationId()).getType());
            }
            if (images.containsKey(response.getNotificationId())) {
                response.setImageId(images.get(response.getNotificationId()).getImageId());
                response.setImageFileName(images.get(response.getNotificationId()).getImageFileName());
            }
            if(personalPriorities.containsKey(response.getNotificationId())) {
                response.setPersonalPriority(true);
            }
        });

        return notificationDetailResponses;
    }
}
