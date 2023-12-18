package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.Notification;
import fpt.capstone.buildingmanagementsystem.model.entity.NotificationReceiver;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationReceiverRepository extends JpaRepository<NotificationReceiver,String> {
    void deleteAllByNotification_NotificationId(String id);
    List<NotificationReceiver> findAllByReceiver(User user);
    List<NotificationReceiver> findByNotification(Notification notification);
}
