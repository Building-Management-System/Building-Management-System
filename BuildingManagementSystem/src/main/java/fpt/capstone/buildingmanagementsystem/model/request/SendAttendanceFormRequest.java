package fpt.capstone.buildingmanagementsystem.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SendAttendanceFormRequest {
    String userId;
    String title;
    String content;
    String manualDate;
    String manualFirstEntry;
    String manualLastExit;
    String departmentId;
    String receivedId;
}
