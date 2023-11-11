package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.mapper.DailyLogMapper;
import fpt.capstone.buildingmanagementsystem.mapper.OvertimeLogMapper;
import fpt.capstone.buildingmanagementsystem.model.entity.DailyLog;
import fpt.capstone.buildingmanagementsystem.model.entity.OvertimeLog;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.response.*;
import fpt.capstone.buildingmanagementsystem.repository.DailyLogRepository;
import fpt.capstone.buildingmanagementsystem.repository.OverTimeRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import fpt.capstone.buildingmanagementsystem.until.Until;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
public class AttendanceService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    DailyLogRepository dailyLogRepository;
    @Autowired
    DailyLogMapper dailyLogMapper;
    @Autowired
    OvertimeLogMapper overtimeLogMapper;
    @Autowired
    OverTimeRepository overTimeRepository;

    public GetAttendanceUserResponse getAttendanceUser(String user_id, int month) {
        try {
            double totalAttendance = 0.0;
            double morningTotal = 0.0;
            double afternoonTotal = 0.0;
            double lateCheckinTotal = 0.0;
            double earlyCheckoutTotal = 0.0;
            double permittedLeave = 0.0;
            double nonPermittedLeave = 0.0;
            double ViolateTotal = 0.0;
            double outsideWork = 0.0;
            double paidDay = 0.0;
            int totalDay = 0;
            String formattedDate = null;
            List<DailyLogResponse> list = new ArrayList<>();
            if (user_id != null) {
                    List<DailyLog> dailyLogs = dailyLogRepository.findByUser_UserIdAndMonth(user_id, month);
                    if (dailyLogs.size() > 0) {
                        for (DailyLog dailyLog : dailyLogs) {
                            totalAttendance = totalAttendance + dailyLog.getTotalAttendance();
                            morningTotal = morningTotal + dailyLog.getMorningTotal();
                            afternoonTotal = afternoonTotal + dailyLog.getAfternoonTotal();
                            if (dailyLog.isEarlyCheckout()) {
                                lateCheckinTotal = lateCheckinTotal + 1.0;
                            }
                            if (dailyLog.isEarlyCheckout()) {
                                earlyCheckoutTotal = earlyCheckoutTotal + 1.0;
                            }
                            if (dailyLog.isViolate()) {
                                ViolateTotal = ViolateTotal + 1.0;
                            }
                            if ((getCheckWeekend(dailyLog.getDate()) != Calendar.SATURDAY) && (getCheckWeekend(dailyLog.getDate()) != Calendar.SUNDAY)) {
                                totalDay = totalDay + 1;
                            }
                            permittedLeave = permittedLeave + dailyLog.getPermittedLeave();
                            nonPermittedLeave = nonPermittedLeave + dailyLog.getNonPermittedLeave();
                            outsideWork = outsideWork + dailyLog.getOutsideWork();
                            paidDay = paidDay + dailyLog.getPaidDay();
                            DailyLogResponse dailyLogResponse = dailyLogMapper.convertGetAttendanceUserResponse(dailyLog);
                            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.US);
                            dailyLogResponse.setDateDaily(sdf.format(Until.convertDateToCalender(dailyLog.getDate()).getTime()));
                            list.add(dailyLogResponse);
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat("MMMM,yyyy", Locale.US);
                        String monthTotal = sdf.format(Until.convertDateToCalender(dailyLogs.get(0).getDate()).getTime());
                        TotalAttendanceUser totalAttendanceUser = TotalAttendanceUser.builder().
                                lateCheckinTotal(lateCheckinTotal).ViolateTotal(ViolateTotal).earlyCheckoutTotal(earlyCheckoutTotal).date(monthTotal)
                                .afternoonTotal(afternoonTotal).morningTotal(morningTotal)
                                .totalAttendance(totalAttendance).nonPermittedLeave(nonPermittedLeave).paidDay(paidDay).permittedLeave(permittedLeave)
                                .outsideWork(outsideWork).totalDate(totalDay).build();
                        return new GetAttendanceUserResponse(dailyLogs.get(0).getUser().getAccount().getUsername()
                                , dailyLogs.get(0).getUser().getDepartment().getDepartmentName(), monthTotal, totalAttendanceUser, list);
                    } else {
                        throw new NotFound("list_null");
                    }
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (ServerError e) {
            throw new ServerError("fail");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public AttendanceDetailResponse getAttendanceDetail(String user_id, String date) {
        if (user_id != null) {
            try {
                DailyLog dailyLogs = dailyLogRepository.getAttendanceDetailByUserIdAndDate(user_id, date)
                        .orElseThrow(() -> new BadRequest("not_found"));
                OvertimeLog overtimeLog = overTimeRepository.getAttendanceDetail(user_id, date);
                OverTimeDetailResponse overTimeDetailResponse = overtimeLogMapper.convertGetAttendanceUserDetailResponse(overtimeLog);
                DailyDetailResponse dailyDetailResponse = dailyLogMapper.convertGetAttendanceUserDetailResponse(dailyLogs);
                return AttendanceDetailResponse.builder().dailyDetailResponse(dailyDetailResponse).date(date).overTimeDetailResponse(overTimeDetailResponse).build();
            } catch (Exception e) {
                throw new ServerError("fail");
            }
        } else {
            throw new BadRequest("request_fail");
        }
    }

    public int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public int getCheckWeekend(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }
}
