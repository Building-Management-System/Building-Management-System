package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.RoomBookingFormRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomBookingFormRoomRepository extends JpaRepository<RoomBookingFormRoom, String> {
}
