package fpt.capstone.buildingmanagementsystem.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendOtherFormRequest {
    String userId;
    String ticketId;
    String title;
    String content;
    String departmentId;
    String receivedId;
}
