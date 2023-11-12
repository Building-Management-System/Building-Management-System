package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.ControlLogLcd;
import fpt.capstone.buildingmanagementsystem.model.entity.DailyLog;
import fpt.capstone.buildingmanagementsystem.model.entity.OvertimeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ControlLogLcdRepository extends JpaRepository<ControlLogLcd, String> {
    @Query(value = "select * from control_log_lcd where persion_name = :persion_name and time between :timeBefore and :timeAfter", nativeQuery = true)
    List<ControlLogLcd> getControlLog(String persion_name,Date timeBefore, Date timeAfter);
    @Query(value = "select * from control_log_lcd where persion_name = :persion_name and DATE(time) = :date", nativeQuery = true)
    List<ControlLogLcd> getControlLogLcdList(String persion_name, String date);
}
