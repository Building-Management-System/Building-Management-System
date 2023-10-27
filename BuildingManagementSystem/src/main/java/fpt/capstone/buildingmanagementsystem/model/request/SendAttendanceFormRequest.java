package fpt.capstone.buildingmanagementsystem.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Time;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SendAttendanceFormRequest {
    String userId;
    String ticketId;
    String requestId;
    String title;
    String content;
    Date manualDate;
    Time manualFirstEntry;
    Time manualLastExit;
    String departmentId;
    String receivedId;
}
