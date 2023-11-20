package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.ChangeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChangeLogRepository extends JpaRepository<ChangeLog, String> {

    @Query(value = "SELECT *\n" +
            "FROM change_log\n" +
            "WHERE employee_id LIKE :userId AND MONTH(date) = :month",nativeQuery = true)
    List<ChangeLog> getChangeLogByUserIdAndMonth(@Param("userId") String userId, @Param("month")int month);
}
