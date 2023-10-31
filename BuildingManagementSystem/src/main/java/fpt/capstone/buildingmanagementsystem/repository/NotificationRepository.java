package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, String> {

    @Query(value = "SELECT n.*\n" +
            "FROM notification n\n" +
            "JOIN unread_mark um ON n.notification_id = um.notification_id\n" +
            "JOIN user u ON u.user_id = um.user_id\n" +
            "WHERE um.user_id LIKE :userId", nativeQuery = true)
    List<Notification> getUnreadMarkNotificationByUserId(@Param("userId") String userId);


    @Query(value = "SELECT n.*\n" +
            "FROM notification n\n" +
            "         JOIN notification_receiver nr ON n.notification_id = nr.notification_id\n" +
            "         LEFT JOIN user u ON u.user_id = nr.receiver_id\n" +
            "WHERE nr.receiver_id LIKE :userId\n" +
            "   OR nr.send_all_status IS TRUE", nativeQuery = true)
    List<Notification> getNotificationByUserId(@Param("userId") String userId);
}
