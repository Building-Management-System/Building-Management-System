package fpt.capstone.buildingmanagementsystem.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TicketDto {
    private String ticketId;
    private String createDate;
    private String updateDate;
    private boolean status;
    //     @Enumerated(EnumType.STRING)
    String topic;
    //        List<RequestTicketDto> requestTicketDtos;
    private String requestId;
    private String title;
    private String requestCreateDate;
    private String requestUpdateDate;
    private String requestStatus;
    private String userId;
}
