package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.model.entity.Notification;
import fpt.capstone.buildingmanagementsystem.model.entity.NotificationFile;
import fpt.capstone.buildingmanagementsystem.model.entity.NotificationImage;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.NotificationStatus;
import fpt.capstone.buildingmanagementsystem.model.response.NotificationDetailResponse;
import fpt.capstone.buildingmanagementsystem.model.response.NotificationFileResponse;
import fpt.capstone.buildingmanagementsystem.model.response.NotificationImageResponse;
import fpt.capstone.buildingmanagementsystem.model.response.NotificationResponse;
import fpt.capstone.buildingmanagementsystem.model.response.NotificationTitleResponse;
import fpt.capstone.buildingmanagementsystem.repository.NotificationFileRepository;
import fpt.capstone.buildingmanagementsystem.repository.NotificationImageRepository;
import fpt.capstone.buildingmanagementsystem.repository.NotificationRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class NotificationServiceV2 {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationFileRepository notificationFileRepository;

    @Autowired
    private NotificationImageRepository notificationImageRepository;

    @Autowired
    private UserRepository userRepository;

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

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFound("not_found_user"));

        Map<String, Notification> hiddenNotification = notificationRepository.getHiddenNotificationByUserId(userId)
                .stream()
                .collect(Collectors.toMap(Notification::getNotificationId, Function.identity()));

        List<Notification> notifications = notificationRepository.getNotificationByUserId(userId)
                .stream().filter(notification -> !hiddenNotification.containsKey(notification.getNotificationId()))
                .collect(Collectors.toList());

        Map<String, List<NotificationFile>> files = notificationFileRepository.findByNotificationIn(notifications)
                .stream()
                .collect(groupingBy(file -> file.getNotification().getNotificationId(), Collectors.toList()));

        Map<String, List<NotificationImage>> images = notificationImageRepository.findByNotificationIn(notifications)
                .stream()
                .collect(groupingBy(image -> image.getNotification().getNotificationId(), Collectors.toList()));

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
            detailResponse.setDepartmentUpload(user.getDepartment());
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
                List<NotificationFileResponse> notificationFiles = files.get(response.getNotificationId())
                        .stream()
                        .map(file -> new NotificationFileResponse(
                                file.getFileId(),
                                file.getName(),
                                file.getType()
                        )).collect(Collectors.toList());
                response.setNotificationFiles(notificationFiles);
            }
            if (images.containsKey(response.getNotificationId())) {
                List<NotificationImageResponse> notificationImages = images.get(response.getNotificationId())
                        .stream()
                        .map(image -> new NotificationImageResponse(image.getImageId(), image.getImageFileName()))
                        .collect(Collectors.toList());
                response.setNotificationImages(notificationImages);
            }
            if (personalPriorities.containsKey(response.getNotificationId())) {
                response.setPersonalPriority(true);
            }
        });

        return notificationDetailResponses;
    }

    public List<NotificationDetailResponse> getListUploadedNotificationByUserId(String userId) {
        return getListNotificationByUserId(userId)
                .stream().filter(notification -> notification.getNotificationStatus().equals(NotificationStatus.UPLOADED))
                .collect(Collectors.toList());
    }

    public List<NotificationDetailResponse> getListDraftNotificationByUserId(String userId) {
        return getListNotificationByUserId(userId)
                .stream().filter(notification -> notification.getNotificationStatus().equals(NotificationStatus.DRAFT))
                .collect(Collectors.toList());
    }

    public List<NotificationDetailResponse> getListScheduledNotificationByUserId(String userId) {
        return getListNotificationByUserId(userId)
                .stream().filter(notification -> notification.getNotificationStatus().equals(NotificationStatus.SCHEDULED))
                .collect(Collectors.toList());
    }
}
