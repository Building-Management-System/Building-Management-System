package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.Device;
import fpt.capstone.buildingmanagementsystem.model.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findAll();
    Room findByDevice(Device deviceId);

    @Query(value = "SELECT *\n" +
            "FROM room r\n" +
            "JOIN device d ON d.device_id = r.device_id\n" +
            "WHERE d.status LIKE 'INACTIVE'\n" +
            "AND r.room_id = :roomId ", nativeQuery = true)
    Optional<Room> getRoomByInActiveDevice(int roomId);

    @Query(value = "SELECT *\n" +
            "FROM room \n" +
            "WHERE device_id LIKE :deviceId;", nativeQuery = true)
    Optional<Room> getRoomByDevice(String deviceId);

}
