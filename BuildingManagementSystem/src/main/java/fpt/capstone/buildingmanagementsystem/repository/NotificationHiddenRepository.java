package fpt.capstone.buildingmanagementsystem.repository;


import fpt.capstone.buildingmanagementsystem.model.entity.NotificationHidden;
import fpt.capstone.buildingmanagementsystem.model.entity.UnreadMark;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationHiddenRepository extends JpaRepository<NotificationHidden,String> {
    void deleteAllByNotification_NotificationId(String id);
    List<NotificationHidden> findAllByUser(User user);

    void deleteAllByUser(User user);

}