package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.Account;
import fpt.capstone.buildingmanagementsystem.model.entity.Department;
import fpt.capstone.buildingmanagementsystem.model.entity.InactiveManagerTemp;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InactiveManagerTempRepository extends JpaRepository<InactiveManagerTemp, String> {
    List<InactiveManagerTemp> findByDepartment(Department department);
    List<InactiveManagerTemp> findAllByManager(Account account);
}
