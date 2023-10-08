package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.OvertimeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;


@Repository
public interface OverTimeRepository extends JpaRepository<OvertimeLog, Long> {
    @Query(value = "select * from overtime_log where user_id = :user_id and date = :date", nativeQuery = true)
    OvertimeLog getAttendanceDetail(String user_id, Date date);
}
