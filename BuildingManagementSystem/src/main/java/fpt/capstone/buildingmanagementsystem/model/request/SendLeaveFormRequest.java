package fpt.capstone.buildingmanagementsystem.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SendLeaveFormRequest {
    String userId;
    String title;
    String content;
    String fromDate;
    String toDate;
    boolean halfDay;
    int durationEvaluation;
    String departmentId;
    String receivedId;
}
