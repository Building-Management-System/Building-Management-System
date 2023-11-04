package fpt.capstone.buildingmanagementsystem.model.response;

import fpt.capstone.buildingmanagementsystem.model.enumEnitty.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationDetailResponseV2 {

    private String notificationId;
    private String title;
    private String content;
    private Date uploadDate;
    private NotificationStatus notificationStatus;
    private boolean priority;
    private String creatorId;
    private String creatorFirstName;
    private String creatorLastName;
    private String creatorImage;
    private boolean personalPriority;

    private List<NotificationFileResponse> notificationFiles;

    private List<NotificationImageResponse> notificationImages;

}
