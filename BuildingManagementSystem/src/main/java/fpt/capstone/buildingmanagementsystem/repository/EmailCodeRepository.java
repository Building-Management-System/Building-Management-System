package fpt.capstone.buildingmanagementsystem.repository;


import fpt.capstone.buildingmanagementsystem.model.entity.EmailCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface EmailCodeRepository extends JpaRepository<EmailCode, String> {
    List<EmailCode> findByCodeAndUserId(String code, String userId);
}
