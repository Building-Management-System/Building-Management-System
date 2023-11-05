package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.model.entity.Notification;
import fpt.capstone.buildingmanagementsystem.model.entity.NotificationReceiver;
import fpt.capstone.buildingmanagementsystem.model.entity.UnreadMark;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.NotificationStatus;
import fpt.capstone.buildingmanagementsystem.model.request.ApprovalNotificationRequest;
import fpt.capstone.buildingmanagementsystem.repository.NotificationReceiverRepository;
import fpt.capstone.buildingmanagementsystem.repository.NotificationRepository;
import fpt.capstone.buildingmanagementsystem.repository.UnreadMarkRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import fpt.capstone.buildingmanagementsystem.until.Until;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutomaticNotificationService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    UnreadMarkRepository unreadMarkRepository;

    @Autowired
    NotificationReceiverRepository notificationReceiverRepository;

    public void sendApprovalRequestNotification(ApprovalNotificationRequest request) {

        String notificationTitle = "[SYSTEM] New update about "+request.getTopic()+" request";

        String notificationContent = "" +
                "Ticket Id: " + request.getTicketId() + "<br>" +
                "Topic: " + request.getTopic() + "<br>";
        if(request.isDecision()) {
            notificationContent += "Status: Accepted";
        } else {
            notificationContent += "" +
                    "Status: Rejected <br>" +
                    "Reason: " + request.getReason();
        }

        Notification notification = Notification.builder()
                .title(notificationTitle)
                .content(notificationContent)
                .notificationStatus(NotificationStatus.UPLOADED)
                .priority(false)
                .createDate(Until.generateRealTime())
                .updateDate(Until.generateRealTime())
                .uploadDate(Until.generateRealTime())
                .createdBy(request.getSensor())
                .build();

        UnreadMark unreadMark = UnreadMark.builder()
                .notification(notification)
                .user(request.getReceiver())
                .build();

        NotificationReceiver notificationReceiver = NotificationReceiver.builder()
                .notification(notification)
                .receiver(request.getReceiver())
                .build();

        try {
            notificationRepository.saveAndFlush(notification);
            unreadMarkRepository.save(unreadMark);
            notificationReceiverRepository.save(notificationReceiver);
        }catch (Exception e) {
            throw new ServerError("Fail");
        }
    }
    public void sendApprovalTicketNotification(ApprovalNotificationRequest request) {

        String notificationTitle = "[SYSTEM] You have a new ticket request.";

        String notificationContent = "" +
                "Ticket Id: " + request.getTicketId() + "<br>" +
                "Topic: " + request.getTopic() + "\n";

        Notification notification = Notification.builder()
                .title(notificationTitle)
                .content(notificationContent)
                .notificationStatus(NotificationStatus.UPLOADED)
                .priority(false)
                .createDate(Until.generateRealTime())
                .updateDate(Until.generateRealTime())
                .uploadDate(Until.generateRealTime())
                .createdBy(request.getSensor())
                .build();

        UnreadMark unreadMark = UnreadMark.builder()
                .notification(notification)
                .user(request.getReceiver())
                .build();

        NotificationReceiver notificationReceiver = NotificationReceiver.builder()
                .notification(notification)
                .receiver(request.getReceiver())
                .build();

        try {
            notificationRepository.saveAndFlush(notification);
            unreadMarkRepository.save(unreadMark);
            notificationReceiverRepository.save(notificationReceiver);
        }catch (Exception e) {
            throw new ServerError("Fail");
        }
    }
}
