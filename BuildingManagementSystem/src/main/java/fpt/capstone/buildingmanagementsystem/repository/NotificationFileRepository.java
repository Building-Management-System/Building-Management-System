package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.Notification;
import fpt.capstone.buildingmanagementsystem.model.entity.NotificationFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationFileRepository extends JpaRepository<NotificationFile,String> {

    List<NotificationFile> findByNotificationIn(List<Notification> notifications);

    List<NotificationFile> findByNotification(Notification notification);
    void deleteAllByNotification_NotificationId(String id);

}
