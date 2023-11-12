package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.mapper.OvertimeLogMapper;
import fpt.capstone.buildingmanagementsystem.model.entity.DailyLog;
import fpt.capstone.buildingmanagementsystem.model.entity.OvertimeLog;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.DateType;
import fpt.capstone.buildingmanagementsystem.model.response.GetOvertimeListResponse;
import fpt.capstone.buildingmanagementsystem.model.response.OverTimeLogResponse;
import fpt.capstone.buildingmanagementsystem.model.response.SystemTimeResponse;
import fpt.capstone.buildingmanagementsystem.repository.DailyLogRepository;
import fpt.capstone.buildingmanagementsystem.repository.OverTimeRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import fpt.capstone.buildingmanagementsystem.until.Until;
import fpt.capstone.buildingmanagementsystem.validate.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class OvertimeService {
    @Autowired
    OvertimeLogMapper overtimeLogMapper;
    @Autowired
    OverTimeRepository overTimeRepository;

    @Autowired
    DailyLogRepository dailyLogRepository;

    @Autowired
    DailyLogService dailyLogService;

    @Autowired
    UserRepository userRepository;

    private static final Time endAfternoonTime = Time.valueOf("17:30:00");

    private static final Time startOverTime = Time.valueOf("18:00:00");


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
                                    .totalAttendance(getTime(element.getManualStart(), element.getManualEnd()))
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

    public SystemTimeResponse getSystemTime(String userId, Date date) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequest("Not_found_user"));

        Optional<DailyLog> dailyLogOptional = dailyLogRepository.findByUserAndDate(user, date);
        if (!dailyLogOptional.isPresent()) return new SystemTimeResponse();
        else {
            DailyLog dailyLog = dailyLogOptional.get();
            DateType dateType = DailyLogService.getDateType(date);
            if (dateType.equals(DateType.NORMAL)) {
                if (Validate.compareTime(dailyLog.getCheckout(), endAfternoonTime) > 0) {
                    return new SystemTimeResponse(date, startOverTime, dailyLog.getCheckout());
                } else {
                    return new SystemTimeResponse(date, null, null);
                }
            } else {
                return new SystemTimeResponse(date, dailyLog.getSystemCheckIn(), dailyLog.getSystemCheckOut());
            }
        }
    }
}
