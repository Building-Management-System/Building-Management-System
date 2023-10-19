package fpt.capstone.buildingmanagementsystem.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketRequestDto {
    private String ticketId;
    private String createDate;
    private String updateDate;
    private boolean status;
    String topic;
    private String requestId;
    private String title;
    private String requestCreateDate;
    private String requestUpdateDate;
    private String requestStatus;
    private String userId;
    private String senderId;
    private String receiverId;
    private String messageCreateDate;
    private String departmentId;
    private String departmentName;
    private String receiverFirstName;
    private String receiverLastName;
}
