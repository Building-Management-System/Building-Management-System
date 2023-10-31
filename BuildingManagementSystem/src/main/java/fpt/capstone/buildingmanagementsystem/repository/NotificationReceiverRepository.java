package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.NotificationReceiver;
import org.springframework.data.jpa.repository.JpaRepository;
public interface NotificationReceiverRepository extends JpaRepository<NotificationReceiver,String> {
}
