package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.model.entity.Notification;
import fpt.capstone.buildingmanagementsystem.model.entity.PersonalPriority;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.NotificationStatus;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.NotificationViewer;
import fpt.capstone.buildingmanagementsystem.model.response.NotificationDetailResponse;
import fpt.capstone.buildingmanagementsystem.model.response.NotificationDetailResponseForDetail;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

    public NotificationDetailResponseForDetail getNotificationDetailByCreator(String creatorId, String notificationId) {
        User user = userRepository.findByUserId(creatorId)
                .orElseThrow(() -> new NotFound("not_found_user"));

        Notification notification = notificationRepository.findByCreatedByAndNotificationId(user, notificationId)
                .orElseThrow(() -> new NotFound("not_found_notification"));

        return getNotificationDetailOfDetail(notificationId, user, notification);
    }

    public NotificationDetailResponseForDetail getNotificationDetailByUserIdAndNotificationId(String userId, String notificationId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFound("not_found_user"));

        List<Notification> hiddenNotification = notificationRepository.getHiddenNotificationByUserIdAndNotification(userId, notificationId);
        if (!hiddenNotification.isEmpty()) {
            throw new NotFound("Not_found_notification");
        }
        Notification notification = notificationRepository.getNotificationByUserIdAndNotificationId(userId, notificationId)
                .orElseThrow(() -> new NotFound("not_found_notification"));

        return getNotificationDetailOfDetail(notificationId, user, notification);
    }

    public List<NotificationDetailResponse> getListNotificationByCreator(String userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFound("not_found_user"));

        List<Notification> notifications = notificationRepository.findByCreatedBy(user);

        return getNotificationResponsesOfList(userId, notifications, NotificationViewer.SENDER);
    }

    public List<NotificationDetailResponse> getListUploadedNotificationByUserId(String userId) {
        Map<String, Notification> hiddenNotification = notificationRepository.getHiddenNotificationByUserId(userId)
                .stream()
                .collect(Collectors.toMap(Notification::getNotificationId, Function.identity()));

        List<Notification> notifications = notificationRepository.getNotificationByUserId(userId)
                .stream().filter(notification -> !hiddenNotification.containsKey(notification.getNotificationId()))
                .collect(Collectors.toList());

        return getNotificationResponsesOfList(userId, notifications, NotificationViewer.RECEIVER);
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

    private List<NotificationDetailResponse> getNotificationResponsesOfList(String userId, List<Notification> notifications, NotificationViewer viewAs) {

        Map<String, Notification> personalPriorities = notificationRepository.getPersonalPriorityByUserId(userId)
                .stream().collect(Collectors.toMap(Notification::getNotificationId, Function.identity()));

        List<NotificationDetailResponse> notificationDetailResponses = new ArrayList<>();

        ExecutorService ex = Executors.newFixedThreadPool(5);

        notifications.forEach(notification -> ex.submit(() -> {
            NotificationDetailResponse detailResponse = new NotificationDetailResponse();
            detailResponse.setNotificationId(notification.getNotificationId());
            detailResponse.setTitle(notification.getTitle());
            detailResponse.setContent(notification.getContent());
            detailResponse.setUploadDate(notification.getUploadDate());
            detailResponse.setNotificationStatus(notification.getNotificationStatus());
            detailResponse.setPriority(notification.isPriority());
            detailResponse.setCreatorId(notification.getCreatedBy().getUserId());
            detailResponse.setDepartmentUpload(notification.getCreatedBy().getDepartment());
            detailResponse.setReadStatus(true);
            detailResponse.setViewAs(viewAs);
            notificationDetailResponses.add(detailResponse);
        }));

        Map<String, Notification> unreadNotification = notificationRepository.getUnreadMarkNotificationByUserId(userId)
                .stream().collect(Collectors.toMap(Notification::getNotificationId, Function.identity()));

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        notificationDetailResponses.forEach(response -> executorService.submit(() -> {
            if (unreadNotification.containsKey(response.getNotificationId())) {
                response.setReadStatus(false);
            }
            if (personalPriorities.containsKey(response.getNotificationId())) {
                response.setPersonalPriority(true);
            }
        }));
        executorService.shutdown();
        return notificationDetailResponses.stream()
                .sorted(Comparator.comparing(NotificationDetailResponse::isPriority))
                .collect(Collectors.toList());
    }

    private NotificationDetailResponseForDetail getNotificationDetailOfDetail(String notificationId, User user, Notification notification) {
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

        NotificationDetailResponseForDetail notificationDetailResponse = NotificationDetailResponseForDetail.builder()
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
//
//    //todo: sửa query lấy thằng data, không chọc vào db
//    public List<NotificationDetailResponseForDetail> getListScheduledNotificationByDepartmentOfCreator(String userId) {
//        User user = userRepository.findByUserId(userId)
//                .orElseThrow(() -> new NotFound("not_found_user"));
//
//        List<Notification> notifications = notificationRepository.getNotificationByDepartment(user.getDepartment().getDepartmentId());
//
//        Map<String, List<NotificationFile>> files = notificationFileRepository.findByNotificationIn(notifications)
//                .stream()
//                .collect(groupingBy(file -> file.getNotification().getNotificationId(), Collectors.toList()));
//
//        Map<String, List<NotificationImage>> images = notificationImageRepository.findByNotificationIn(notifications)
//                .stream()
//                .collect(groupingBy(image -> image.getNotification().getNotificationId(), Collectors.toList()));
//
//        List<NotificationDetailResponseForDetail> notificationDetailResponses = new ArrayList<>();
//
//        ExecutorService executorService1 = Executors.newFixedThreadPool(5);
//        notifications.forEach(notification -> executorService1.submit(() ->{
//            NotificationDetailResponseForDetail detailResponse = new NotificationDetailResponseForDetail();
//            detailResponse.setNotificationId(notification.getNotificationId());
//            detailResponse.setTitle(notification.getTitle());
//            detailResponse.setContent(notification.getContent());
//            detailResponse.setUploadDate(notification.getUploadDate());
//            detailResponse.setNotificationStatus(notification.getNotificationStatus());
//            detailResponse.setPriority(notification.isPriority());
//            detailResponse.setCreatorId(notification.getCreatedBy().getUserId());
//            detailResponse.setCreatorFirstName(notification.getCreatedBy().getFirstName());
//            detailResponse.setCreatorLastName(notification.getCreatedBy().getLastName());
//
//            notificationDetailResponses.add(detailResponse);
//        }));
//        ExecutorService executorService = Executors.newFixedThreadPool(5);
//
//        notificationDetailResponses.forEach(response -> executorService.submit(() -> {
//
//            if (files.containsKey(response.getNotificationId())) {
//                List<NotificationFileResponse> notificationFiles = files.get(response.getNotificationId())
//                        .stream()
//                        .map(file -> new NotificationFileResponse(
//                                file.getFileId(),
//                                file.getData(),
//                                file.getName(),
//                                file.getType()
//                        )).collect(Collectors.toList());
//                response.setNotificationFiles(notificationFiles);
//            }
//            if (images.containsKey(response.getNotificationId())) {
//                List<NotificationImageResponse> notificationImages = images.get(response.getNotificationId())
//                        .stream()
//                        .map(image -> new NotificationImageResponse(image.getImageId(), image.getImageFileName()))
//                        .collect(Collectors.toList());
//                response.setNotificationImages(notificationImages);
//            }
//        }));
//        executorService.shutdown();
//        executorService1.shutdown();
//        return notificationDetailResponses;
//    }

}
