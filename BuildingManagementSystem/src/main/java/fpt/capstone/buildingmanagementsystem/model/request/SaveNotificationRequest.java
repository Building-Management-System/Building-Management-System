package fpt.capstone.buildingmanagementsystem.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveNotificationRequest {
    String buttonStatus;
    String userId;
    String title;
    boolean sendAllStatus;
    String receiverId;
    boolean priority;
    String content;
    String uploadDate;
}
