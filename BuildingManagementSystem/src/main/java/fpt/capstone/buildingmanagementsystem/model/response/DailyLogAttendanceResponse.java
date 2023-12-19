package fpt.capstone.buildingmanagementsystem.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.Time;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DailyLogAttendanceResponse {
    private String userId;
    private Date date;
    private Time dailyCheckIn;
    private Time dailyCheckOut;
}
