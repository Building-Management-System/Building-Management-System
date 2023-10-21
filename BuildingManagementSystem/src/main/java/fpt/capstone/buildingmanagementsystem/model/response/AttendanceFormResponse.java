package fpt.capstone.buildingmanagementsystem.model.response;

import fpt.capstone.buildingmanagementsystem.model.enumEnitty.TopicEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AttendanceFormResponse {

    private String attendanceRequestId;

    private String manualDate;

    private String manualFirstEntry;

    private String manualLastExit;

    private String content;

    private String requestMessageId;

    private TopicEnum topic;

}
