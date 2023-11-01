package fpt.capstone.buildingmanagementsystem.model.response;

import fpt.capstone.buildingmanagementsystem.model.enumEnitty.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationDetailResponse {

    private String notificationId;
    private String title;
    private String content;
    private Date uploadDate;
    private NotificationStatus notificationStatus;
    private boolean priority;
    private String creatorId;
    private String creatorFirstName;
    private String creatorLastName;
    private boolean readStatus;
    private boolean personalPriority;

    private String fileId;
    private String fileName;
    private String type;

    private String imageId;
    private String imageFileName;

}
