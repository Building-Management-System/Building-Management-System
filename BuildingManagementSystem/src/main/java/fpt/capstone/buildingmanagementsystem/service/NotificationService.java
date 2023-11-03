package fpt.capstone.buildingmanagementsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.mapper.NotificationMapper;
import fpt.capstone.buildingmanagementsystem.model.entity.Notification;
import fpt.capstone.buildingmanagementsystem.model.entity.NotificationImage;
import fpt.capstone.buildingmanagementsystem.model.entity.NotificationReceiver;
import fpt.capstone.buildingmanagementsystem.model.entity.PersonalPriority;
import fpt.capstone.buildingmanagementsystem.model.entity.UnreadMark;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.request.SaveNotificationRequest;
import fpt.capstone.buildingmanagementsystem.repository.NotificationFileRepository;
import fpt.capstone.buildingmanagementsystem.repository.NotificationImageRepository;
import fpt.capstone.buildingmanagementsystem.repository.NotificationReceiverRepository;
import fpt.capstone.buildingmanagementsystem.repository.NotificationRepository;
import fpt.capstone.buildingmanagementsystem.repository.PersonalPriorityRepository;
import fpt.capstone.buildingmanagementsystem.repository.UnreadMarkRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import fpt.capstone.buildingmanagementsystem.until.Until;
import fpt.capstone.buildingmanagementsystem.validate.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static fpt.capstone.buildingmanagementsystem.model.enumEnitty.NotificationStatus.DRAFT;
import static fpt.capstone.buildingmanagementsystem.model.enumEnitty.NotificationStatus.SCHEDULED;
import static fpt.capstone.buildingmanagementsystem.model.enumEnitty.NotificationStatus.UPLOADED;

@Service
public class NotificationService {
    @Autowired
    NotificationReceiverRepository notificationReceiverRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    NotificationMapper notificationMapper;
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    UnreadMarkRepository unreadMarkRepository;
    @Autowired
    NotificationImageRepository notificationImageRepository;
    @Autowired
    NotificationFileRepository notificationFileRepository;
    @Autowired
    FileService fileService;

    @Autowired
    PersonalPriorityRepository personalPriorityRepository;

    public boolean saveNotification(String data, MultipartFile[] image, MultipartFile[] file) {
        try {
            List<NotificationReceiver> notificationReceiverList = new ArrayList<>();
            List<UnreadMark> unreadMarkList = new ArrayList<>();
            List<NotificationImage> listImage = new ArrayList<>();
            List<Optional<User>> receivers = new ArrayList<>();

            SaveNotificationRequest saveNotificationRequest = new ObjectMapper().readValue(data, SaveNotificationRequest.class);
            String userId = saveNotificationRequest.getUserId();
            List<String> receiverId = saveNotificationRequest.getReceiverId();
            String title = saveNotificationRequest.getTitle();
            String content = saveNotificationRequest.getContent();
            String uploadDate = saveNotificationRequest.getUploadDatePlan();
            String buttonStatus = saveNotificationRequest.getButtonStatus();
            boolean sendAllStatus = saveNotificationRequest.isSendAllStatus();

            if ((userId != null && title != null && content != null) &&
                    ((!receiverId.isEmpty() && !sendAllStatus) || (receiverId.isEmpty() && sendAllStatus))) {
                Notification notification = notificationMapper.convert(saveNotificationRequest);
                if (uploadDate == null && buttonStatus.equals("upload")) {
                    notification.setNotificationStatus(UPLOADED);
                }
                if (uploadDate != null && buttonStatus.equals("upload")) {
                    notification.setNotificationStatus(SCHEDULED);
                }
                if (buttonStatus.equals("save")) {
                    notification.setNotificationStatus(DRAFT);
                }
                setUploadDate(uploadDate, notification);
                Optional<User> sender = userRepository.findByUserId(saveNotificationRequest.getUserId());
                receiverId.forEach(element -> receivers.add(userRepository.findByUserId(element)));
                if (sender.isPresent()) {
                    notification.setCreatedBy(sender.get());
                    notificationRepository.save(notification);
                    saveImageAndFileAndReceiver(image, file, sendAllStatus, notification, receivers, notificationReceiverList, unreadMarkList, listImage);
                    return true;
                } else {
                    throw new NotFound("not_found");
                }
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (ServerError | IOException | ParseException e) {
            throw new ServerError("fail");
        }
    }

    private static void setUploadDate(String uploadDate, Notification notification) throws ParseException {
        if (uploadDate != null) {
            if (Validate.validateDateAndTime(uploadDate) && Validate.checkUploadDateRealTime(uploadDate)) {
                notification.setUploadDate(Until.convertStringToDateTime(uploadDate));
            } else {
                throw new BadRequest("request_fail");
            }
        }
    }

    private void saveImageAndFileAndReceiver(MultipartFile[] image, MultipartFile[] file, boolean sendAllStatus, Notification notification, List<Optional<User>> receivers, List<NotificationReceiver> notificationReceiverList, List<UnreadMark> unreadMarkList, List<NotificationImage> listImage) throws IOException {
        if (receivers.size() > 0) {
            receivers.forEach(receiver -> notificationReceiverList.add(
                    setNotificationReceiver(sendAllStatus, notification, receiver)));
            notificationReceiverRepository.saveAll(notificationReceiverList);
            receivers.forEach(receiver -> unreadMarkList.add(
                    setUnreadMark(notification, receiver)));
            unreadMarkRepository.saveAll(unreadMarkList);
        } else {
            notificationReceiverRepository.save(setNotificationReceiver(sendAllStatus, notification, Optional.empty()));
        }
        if (file.length > 0) {
            notificationFileRepository.saveAll(fileService.store(file, notification));
        }
        if (image.length > 0) {
            setListImage(image, notification, listImage);
            notificationImageRepository.saveAll(listImage);
        }
    }

    private static void setListImage(MultipartFile[] image, Notification notification, List<NotificationImage> listImage) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (MultipartFile multipartFile : image) {
            executorService.submit(() -> {
                String imageName = "notification_" + UUID.randomUUID();
                String[] subFileName = Objects.requireNonNull(multipartFile.getOriginalFilename()).split("\\.");
                List<String> stringList = new ArrayList<>(Arrays.asList(subFileName));
                imageName = imageName + "." + stringList.get(stringList.size() - 1);
                Bucket bucket = StorageClient.getInstance().bucket();
                try {
                    bucket.create(imageName, multipartFile.getBytes(), multipartFile.getContentType());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                NotificationImage notificationImage = new NotificationImage();
                notificationImage.setImageFileName(imageName);
                notificationImage.setNotification(notification);
                listImage.add(notificationImage);
            });
            executorService.shutdown();
        }
    }

    private NotificationReceiver setNotificationReceiver(boolean sendAllStatus, Notification notification, Optional<User> receiver) {
        NotificationReceiver notificationReceiver = new NotificationReceiver();
        notificationReceiver.setNotification(notification);
        receiver.ifPresent(notificationReceiver::setReceiver);
        notificationReceiver.setSendAllStatus(sendAllStatus);
        return notificationReceiver;
    }

    private UnreadMark setUnreadMark(Notification notification, Optional<User> receiver) {
        UnreadMark unreadMark = new UnreadMark();
        unreadMark.setNotification(notification);
        receiver.ifPresent(unreadMark::setUser);
        return unreadMark;
    }

    public boolean markAsRead(String notificationId, String userId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BadRequest("Not_found_notification"));

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequest("Not_found_user"));

        UnreadMark unreadMark = unreadMarkRepository.findByNotificationAndUser(notification, user)
                .orElseThrow(() -> new BadRequest("Not_found_unread_notification"));
        try {
            unreadMarkRepository.delete(unreadMark);
            return true;
        } catch (Exception e) {
            throw new ServerError("fail");
        }
    }

    public boolean markAsUnRead(String notificationId, String userId) {

        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BadRequest("Not_found_notification"));

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequest("Not_found_user"));

        UnreadMark unreadMark = UnreadMark.builder()
                .notification(notification)
                .user(user)
                .build();
        try {
            unreadMarkRepository.save(unreadMark);
            return true;
        } catch (Exception e) {
            throw new ServerError("fail");
        }
    }

    public boolean setPersonalPriority(String notificationId, String userId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new BadRequest("Not_found_notification"));

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequest("Not_found_user"));

        PersonalPriority personalPriority = PersonalPriority.builder()
                .user(user)
                .notification(notification)
                .build();
        try {
            personalPriorityRepository.save(personalPriority);
            return true;
        } catch (Exception e) {
            throw new ServerError("fail");
        }
    }
}
