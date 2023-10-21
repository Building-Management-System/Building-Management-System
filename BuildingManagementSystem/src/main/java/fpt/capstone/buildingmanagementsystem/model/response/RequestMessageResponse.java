package fpt.capstone.buildingmanagementsystem.model.response;

import fpt.capstone.buildingmanagementsystem.model.entity.Department;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestMessageResponse {
    private String requestMessageId;

    private String createDate;

    private String attachmentMessageId;

    private String senderId;

    private String receiverId;

    private String requestId;

    private Department receiverDepartment;


}
