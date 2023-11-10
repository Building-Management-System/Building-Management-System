package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.DailyLog;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailyLogRepository extends JpaRepository<DailyLog, Long> {
    @Query(value = "select * from daily_log where user_id = :user_id order by date Desc", nativeQuery = true)
    List<DailyLog> getAttendanceUser(String user_id);
    @Query(value = "select * from daily_log where user_id = :user_id and date = :date", nativeQuery = true)
    Optional<DailyLog> getAttendanceDetailByUserIdAndDate(String user_id, String date);
    List<DailyLog> findAllByUser(User user);

    Optional<DailyLog> findByUserAndDate(User user, Date date);
}
