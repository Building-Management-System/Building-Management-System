package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.Notification;
import fpt.capstone.buildingmanagementsystem.model.entity.NotificationImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationImageRepository extends JpaRepository<NotificationImage,String> {

    List<NotificationImage> findByNotificationIn(List<Notification> notifications);

    List<NotificationImage> findByNotification(Notification notification);
}
