package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
}
