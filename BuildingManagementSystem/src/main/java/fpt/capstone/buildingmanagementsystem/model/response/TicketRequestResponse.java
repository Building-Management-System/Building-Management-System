package fpt.capstone.buildingmanagementsystem.model.response;

import fpt.capstone.buildingmanagementsystem.model.dto.RequestTicketDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TicketRequestResponse {
    private String ticketId;
    private String createDate;
    private String updateDate;
    private boolean status;
    List<RequestTicketDto> requestTicketDtos;

}
