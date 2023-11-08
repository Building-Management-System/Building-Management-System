package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.requestForm.LateRequestForm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LateRequestFormRepository extends JpaRepository<LateRequestForm, String> {
}
