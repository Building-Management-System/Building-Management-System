package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.mapper.OvertimeLogMapper;
import fpt.capstone.buildingmanagementsystem.model.entity.DailyLog;
import fpt.capstone.buildingmanagementsystem.model.entity.OvertimeLog;
import fpt.capstone.buildingmanagementsystem.model.response.*;
import fpt.capstone.buildingmanagementsystem.repository.OverTimeRepository;
import fpt.capstone.buildingmanagementsystem.until.Until;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@Service
public class OvertimeService {
    @Autowired
    OvertimeLogMapper overtimeLogMapper;
    @Autowired
    OverTimeRepository overTimeRepository;

    public GetOvertimeListResponse getOvertime(String user_id, String month, String year) {
        try {
            List<OverTimeLogResponse> list = new ArrayList<>();
            if (user_id != null) {
                List<OvertimeLog> overtimeLog1 = overTimeRepository.getOvertimeLog(user_id, month, year);
                if (overtimeLog1.size() > 0) {
                    overtimeLog1.forEach(element -> {
                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.US);
                        OverTimeLogResponse overtimeLog = null;
                        try {
                            overtimeLog = OverTimeLogResponse.builder().systemCheckIn(element.getStartTime())
                                    .systemCheckOut(element.getEndTime()).checkin(element.getManualStart()).checkout(element.getManualEnd())
                                    .date(sdf.format(Until.convertDateToCalender(element.getDate()).getTime())).dateType(element.getDateType())
                                    .totalAttendance(getTime(element.getStartTime(), element.getManualEnd()))
                                    .insideHours(getTime(element.getManualStart(), element.getEndTime()))
                                    .totalPaid(element.getTotalPaid()).approveDate(element.getApprovedDate()).build();
                        } catch (ParseException e) {
                            throw new ServerError("fail");
                        }
                        list.add(overtimeLog);
                    });
                    SimpleDateFormat sdf = new SimpleDateFormat("MMMM,yyyy", Locale.US);
                    String monthTotal = sdf.format(Until.convertDateToCalender(overtimeLog1.get(0).getDate()).getTime());
                    return new GetOvertimeListResponse(overtimeLog1.get(0).getUser().getAccount().getUsername(), overtimeLog1.get(0).getUser().getDepartment().getDepartmentName(), monthTotal, list);
                } else {
                    throw new NotFound("list_null");
                }
            } else {
                throw new NotFound("user_id_null");
            }
        } catch (ServerError | ParseException e) {
            throw new ServerError("fail");
        }
    }

    public double getTime(Time time1, Time time2) {
        LocalTime timeConvert1 = LocalTime.parse(time1.toString());
        LocalTime timeConvert2 = LocalTime.parse(time2.toString());
        Duration duration = Duration.between(timeConvert1, timeConvert2);
        long totalSeconds = duration.getSeconds();
        String roundedValue = String.format("%.2f", (double) totalSeconds / 3600);
        return Double.parseDouble(roundedValue);
    }
}
