package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.model.entity.Notification;
import fpt.capstone.buildingmanagementsystem.model.entity.NotificationFile;
import fpt.capstone.buildingmanagementsystem.model.entity.NotificationImage;
import fpt.capstone.buildingmanagementsystem.model.entity.PersonalPriority;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.NotificationStatus;
import fpt.capstone.buildingmanagementsystem.model.response.NotificationDetailResponse;
import fpt.capstone.buildingmanagementsystem.model.response.NotificationDetailResponseV2;
import fpt.capstone.buildingmanagementsystem.model.response.NotificationFileResponse;
import fpt.capstone.buildingmanagementsystem.model.response.NotificationImageResponse;
import fpt.capstone.buildingmanagementsystem.model.response.NotificationResponse;
import fpt.capstone.buildingmanagementsystem.model.response.NotificationTitleResponse;
import fpt.capstone.buildingmanagementsystem.repository.NotificationFileRepository;
import fpt.capstone.buildingmanagementsystem.repository.NotificationImageRepository;
import fpt.capstone.buildingmanagementsystem.repository.NotificationRepository;
import fpt.capstone.buildingmanagementsystem.repository.PersonalPriorityRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    @Autowired
    private PersonalPriorityRepository personalPriorityRepository;

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
                        true,
                        notification.getCreatedBy().getUserId(),
                        notification.getCreatedBy().getDepartment()
                )).collect(Collectors.toList());

        Map<String, Notification> unreadNotification = notificationRepository.getUnreadMarkNotificationByUserId(userId)
                .stream().collect(Collectors.toMap(Notification::getNotificationId, Function.identity()));

        notifications.forEach(response -> {
            if (unreadNotification.containsKey(response.getNotificationId())) response.setReadStatus(false);
        });
        return new NotificationTitleResponse(notifications.size(), notifications);
    }

    public NotificationDetailResponseV2 getNotificationDetailByCreator(String creatorId, String notificationId) {
        User user = userRepository.findByUserId(creatorId)
                .orElseThrow(() -> new NotFound("not_found_user"));

        Notification notification = notificationRepository.findByCreatedByAndNotificationId(user, notificationId)
                .orElseThrow(() -> new NotFound("not_found_notification"));

        return getNotificationDetail(notificationId, user, notification);
    }

    public NotificationDetailResponseV2 getNotificationDetailByUserIdAndNotificationId(String userId, String notificationId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFound("not_found_user"));

        List<Notification> hiddenNotification = notificationRepository.getHiddenNotificationByUserIdAndNotification(userId, notificationId);
        if (!hiddenNotification.isEmpty()) {
            throw new NotFound("Not_found_notification");
        }
        Notification notification = notificationRepository.getNotificationByUserIdAndNotificationId(userId, notificationId)
                .orElseThrow(() -> new NotFound("not_found_notification"));

        return getNotificationDetail(notificationId, user, notification);
    }

    public List<NotificationDetailResponse> getListNotificationByCreator(String userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFound("not_found_user"));

        List<Notification> notifications = notificationRepository.findByCreatedBy(user);

        return getNotificationDetailResponses(userId, notifications);
    }

    public List<NotificationDetailResponse> getListUploadedNotificationByUserId(String userId) {
        Map<String, Notification> hiddenNotification = notificationRepository.getHiddenNotificationByUserId(userId)
                .stream()
                .collect(Collectors.toMap(Notification::getNotificationId, Function.identity()));

        List<Notification> notifications = notificationRepository.getNotificationByUserId(userId)
                .stream().filter(notification -> !hiddenNotification.containsKey(notification.getNotificationId()))
                .collect(Collectors.toList());

        return getNotificationDetailResponses(userId, notifications);
    }

    public List<NotificationDetailResponse> getListUploadedNotificationByCreator(String userId) {
        return getListNotificationByCreator(userId)
                .stream().filter(notification -> notification.getNotificationStatus().equals(NotificationStatus.DRAFT))
                .collect(Collectors.toList());
    }

    public List<NotificationDetailResponse> getListDraftNotificationByCreator(String userId) {
        return getListNotificationByCreator(userId)
                .stream().filter(notification -> notification.getNotificationStatus().equals(NotificationStatus.DRAFT))
                .collect(Collectors.toList());
    }

    public List<NotificationDetailResponse> getListScheduledNotificationByCreator(String userId) {
        return getListNotificationByCreator(userId)
                .stream().filter(notification -> notification.getNotificationStatus().equals(NotificationStatus.SCHEDULED))
                .collect(Collectors.toList());
    }

    private List<NotificationDetailResponse> getNotificationDetailResponses(String userId, List<Notification> notifications) {
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
            detailResponse.setDepartmentUpload(notification.getCreatedBy().getDepartment());
            detailResponse.setCreatorFirstName(notification.getCreatedBy().getFirstName());
            detailResponse.setCreatorLastName(notification.getCreatedBy().getLastName());
            detailResponse.setReadStatus(true);

            notificationDetailResponses.add(detailResponse);
        });

        Map<String, Notification> unreadNotification = notificationRepository.getUnreadMarkNotificationByUserId(userId)
                .stream().collect(Collectors.toMap(Notification::getNotificationId, Function.identity()));

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        notificationDetailResponses.forEach(response -> executorService.submit(() -> {
            if (unreadNotification.containsKey(response.getNotificationId())) {
                response.setReadStatus(false);
            }
            if (files.containsKey(response.getNotificationId())) {
                List<NotificationFileResponse> notificationFiles = files.get(response.getNotificationId())
                        .stream()
                        .map(file -> new NotificationFileResponse(
                                file.getFileId(),
                                file.getData(),
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
        }));
        executorService.shutdown();
        return notificationDetailResponses;
    }

    private NotificationDetailResponseV2 getNotificationDetail(String notificationId, User user, Notification notification) {
        List<NotificationFileResponse> notificationFiles = notificationFileRepository.findByNotification(notification)
                .stream().map(file -> new NotificationFileResponse(
                        file.getFileId(),
                        file.getData(),
                        file.getName(),
                        file.getType()
                )).collect(Collectors.toList());
        List<NotificationImageResponse> notificationImages = notificationImageRepository.findByNotification(notification)
                .stream()
                .map(image -> new NotificationImageResponse(image.getImageId(), image.getImageFileName()))
                .collect(Collectors.toList());

        Optional<PersonalPriority> personalPriority = personalPriorityRepository.findByNotificationAndUser(notification, user);

        NotificationDetailResponseV2 notificationDetailResponse = NotificationDetailResponseV2.builder()
                .notificationId(notificationId)
                .title(notification.getTitle())
                .content(notification.getContent())
                .uploadDate(notification.getUploadDate())
                .notificationStatus(notification.getNotificationStatus())
                .priority(notification.isPriority())
                .creatorId(notification.getCreatedBy().getUserId())
                .creatorFirstName(notification.getCreatedBy().getFirstName())
                .creatorLastName(notification.getCreatedBy().getLastName())
                .notificationFiles(notificationFiles)
                .notificationImages(notificationImages)
                .build();
        if (personalPriority.isPresent()) {
            notificationDetailResponse.setPersonalPriority(true);
        }

        return notificationDetailResponse;
    }

}
