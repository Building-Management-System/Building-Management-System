package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.ControlLogLcd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ControlLogLcdRepository extends JpaRepository<ControlLogLcd, String> {
    @Query(value = "select * from control_log_lcd where persion_name = :persion_name and DATE(time) = :date order by time desc", nativeQuery = true)
    List<ControlLogLcd> getControlLogLcdList(String persion_name, String date);
}
