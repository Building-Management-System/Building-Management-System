package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.dto.TicketDto;
import fpt.capstone.buildingmanagementsystem.model.dto.TicketRequestDto;

import java.util.List;

public interface TicketRepositoryv2 {
     List<TicketDto> getAllTicketRequest();

     List<TicketRequestDto> getTicketRequestv2();

     List<TicketRequestDto> getTicketRequestByHr();

     List<TicketRequestDto> getTicketRequestBySecurity();

     List<TicketRequestDto> getTicketRequestByAdmin();

     List<TicketRequestDto> getTicketRequestByDepartmentManager(String managerId);

}
