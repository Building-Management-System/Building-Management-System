package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.RequestMessage;
import fpt.capstone.buildingmanagementsystem.model.entity.requestForm.LeaveRequestForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRequestFormRepository extends JpaRepository<LeaveRequestForm,String> {
    List<LeaveRequestForm> findByRequestMessageIn(List<RequestMessage> requestMessages);
}
