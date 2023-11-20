package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.RequestMessage;
import fpt.capstone.buildingmanagementsystem.model.entity.requestForm.WorkingOutsideRequestForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkingOutsideFormRepository extends JpaRepository<WorkingOutsideRequestForm, String> {
    List<WorkingOutsideRequestForm> findByRequestMessageIn(List<RequestMessage> requestMessages);

}
