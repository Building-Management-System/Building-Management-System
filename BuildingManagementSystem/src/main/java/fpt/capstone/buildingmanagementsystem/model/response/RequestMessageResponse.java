package fpt.capstone.buildingmanagementsystem.model.response;

import fpt.capstone.buildingmanagementsystem.model.entity.Department;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestMessageResponse {
    private String requestMessageId;

    private String createDate;

    private String attachmentMessageId;

    private String senderId;

    private String title;

    private RequestStatus requestTicketStatus;

    private String senderFirstName;

    private String senderLastName;

    private String receiverId;

    private String receiverFirstName;

    private String receiverLastName;

    private String requestId;

    private Department receiverDepartment;

    @Override
    public String toString() {
        return "Detail";
    }
}
