package fpt.capstone.buildingmanagementsystem.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.mapper.NotificationMapper;
import fpt.capstone.buildingmanagementsystem.model.entity.Notification;
import fpt.capstone.buildingmanagementsystem.model.entity.NotificationReceiver;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.request.SaveNotificationRequest;
import fpt.capstone.buildingmanagementsystem.repository.NotificationReceiverRepository;
import fpt.capstone.buildingmanagementsystem.repository.NotificationRepository;
import fpt.capstone.buildingmanagementsystem.repository.UnreadMarkRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Optional;

import static fpt.capstone.buildingmanagementsystem.model.enumEnitty.NotificationStatus.*;

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
    public boolean saveNotification(String data, MultipartFile[] image, MultipartFile[] file) throws IOException {
        try {
            SaveNotificationRequest saveNotificationRequest = new ObjectMapper().readValue(data, SaveNotificationRequest.class);
            String userId= saveNotificationRequest.getUserId();
            String receiverId= saveNotificationRequest.getReceiverId();
            String title= saveNotificationRequest.getTitle();
            String content= saveNotificationRequest.getContent();
            String uploadDate= saveNotificationRequest.getUploadDate();
            String buttonStatus= saveNotificationRequest.getButtonStatus();
            boolean sendAllStatus= saveNotificationRequest.isSendAllStatus();
            if (userId != null && title != null && content != null) {
                if(receiverId != null || sendAllStatus){
                    Notification notification= notificationMapper.convert(saveNotificationRequest);
                    if(uploadDate==null && buttonStatus.equals("save")) {
                        notification.setNotificationStatus(UPLOADED);
                    }
                    if(uploadDate!=null && buttonStatus.equals("save")) {
                        notification.setNotificationStatus(SCHEDULED);
                    }else {
                        notification.setNotificationStatus(DRAFT);
                    }
                    Optional<User> user=userRepository.findByUserId(saveNotificationRequest.getUserId());
                    Optional<User> receiver=userRepository.findByUserId(saveNotificationRequest.getReceiverId());
                    if(user.isPresent()&&receiver.isPresent()){
                        notification.setCreatedBy(user.get());
                        notificationRepository.save(notification);
                        saveNotificationReceiver(sendAllStatus, notification, receiver);
                        return true;
                    }else {
                        throw new NotFound("not_found");
                    }
                } else {
                    throw new BadRequest("request_fail");
                }
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (Exception e) {
            throw new ServerError("fail");
        }
    }

    private void saveNotificationReceiver(boolean sendAllStatus, Notification notification, Optional<User> receiver) {
        NotificationReceiver notificationReceiver= new NotificationReceiver();
        notificationReceiver.setNotification(notification);
        notificationReceiver.setReceiver(receiver.get());
        notificationReceiver.setSendAllStatus(sendAllStatus);
        notificationReceiverRepository.save(notificationReceiver);
    }
}
