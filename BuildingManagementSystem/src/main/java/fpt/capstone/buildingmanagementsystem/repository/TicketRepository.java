package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.dto.TicketDto;

import java.util.List;

public interface TicketRepository {
     List<TicketDto> getAllTicketRequest();
}
