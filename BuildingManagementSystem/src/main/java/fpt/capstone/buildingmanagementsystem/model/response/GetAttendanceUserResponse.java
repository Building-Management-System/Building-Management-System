package fpt.capstone.buildingmanagementsystem.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
import java.sql.Time;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAttendanceUserResponse{
    private String date;
    private String firstEntry;
    private String lastExit;
    private float totalTime;
}
