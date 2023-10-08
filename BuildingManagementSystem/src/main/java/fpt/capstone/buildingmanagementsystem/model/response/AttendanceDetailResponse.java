package fpt.capstone.buildingmanagementsystem.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceDetailResponse {
    Date date;
    DailyDetailResponse dailyDetailResponse;
    OverTimeDetailResponse overTimeDetailResponse;
}
