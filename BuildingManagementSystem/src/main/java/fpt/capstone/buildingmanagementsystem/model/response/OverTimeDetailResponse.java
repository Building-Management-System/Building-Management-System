package fpt.capstone.buildingmanagementsystem.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverTimeDetailResponse {
    private Time startTime;
    private Time endTime;
    private String dateType;
    private Time manualStart;

    private Time manualEnd;
    private String description;
}
