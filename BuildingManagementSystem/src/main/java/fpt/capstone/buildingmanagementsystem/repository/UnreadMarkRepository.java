package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.UnreadMark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UnreadMarkRepository extends JpaRepository<UnreadMark,String> {
}
