package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.RequestMessage;
import fpt.capstone.buildingmanagementsystem.model.entity.requestForm.RoomBookingRequestForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomBookingFormRepository extends JpaRepository<RoomBookingRequestForm, String> {

    List<RoomBookingRequestForm> findByRequestMessageIn(List<RequestMessage> requestMessages);

}
