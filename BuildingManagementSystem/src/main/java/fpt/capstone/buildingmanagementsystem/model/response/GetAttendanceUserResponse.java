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
    private Date date;
    private Time firstEntry;
    private Time lastExit;
    private float totalTime;
}
