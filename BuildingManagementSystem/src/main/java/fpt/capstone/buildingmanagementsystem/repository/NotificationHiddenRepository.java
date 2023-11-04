package fpt.capstone.buildingmanagementsystem.repository;


import fpt.capstone.buildingmanagementsystem.model.entity.NotificationHidden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface NotificationHiddenRepository extends JpaRepository<NotificationHidden,String> {
    void deleteAllByNotification_NotificationId(String id);

}