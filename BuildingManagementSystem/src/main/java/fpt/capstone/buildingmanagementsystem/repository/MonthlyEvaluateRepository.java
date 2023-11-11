package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.MonthlyEvaluate;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonthlyEvaluateRepository extends JpaRepository<MonthlyEvaluate, String> {
    Optional<MonthlyEvaluate> findByEmployeeAndMonthAndYear(User user, int month, int year);
}
