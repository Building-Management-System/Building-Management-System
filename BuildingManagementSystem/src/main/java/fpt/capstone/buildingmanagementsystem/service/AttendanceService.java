package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.mapper.DailyLogMapper;
import fpt.capstone.buildingmanagementsystem.model.entity.ControlLogLcd;
import fpt.capstone.buildingmanagementsystem.model.entity.DailyLog;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.response.AttendanceDetailResponse;
import fpt.capstone.buildingmanagementsystem.model.response.ControlLogResponse;
import fpt.capstone.buildingmanagementsystem.model.response.DailyLogResponse;
import fpt.capstone.buildingmanagementsystem.model.response.GetAttendanceUserResponse;
import fpt.capstone.buildingmanagementsystem.model.response.TotalAttendanceUser;
import fpt.capstone.buildingmanagementsystem.repository.ControlLogLcdRepository;
import fpt.capstone.buildingmanagementsystem.repository.DailyLogRepository;
import fpt.capstone.buildingmanagementsystem.service.schedule.CheckoutAnalyzeSchedule;
import fpt.capstone.buildingmanagementsystem.until.Until;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


@Service
public class AttendanceService {
    @Autowired
    DailyLogRepository dailyLogRepository;
    @Autowired
    DailyLogMapper dailyLogMapper;
    @Autowired
    ControlLogLcdRepository controlLogLcdRepository;

    @Autowired
    CheckoutAnalyzeSchedule checkoutAnalyzeSchedule;

    @Autowired
    DailyLogService dailyLogService;

    public GetAttendanceUserResponse getAttendanceUser(String user_id, int month, String year) {
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
            List<DailyLogResponse> list = new ArrayList<>();
            if (user_id != null) {
                List<DailyLog> dailyLogs = dailyLogRepository.getByUserIdAndMonthAndYear(user_id, month, year);
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
                Optional<DailyLog> dailyLogOptional = dailyLogRepository.getAttendanceDetailByUserIdAndDate(user_id, date);
                if (dailyLogOptional.isPresent()) {
                    DailyLog dailyLogs = dailyLogOptional.get();
                    String name = dailyLogs.getUser().getFirstName() + " " + dailyLogs.getUser().getLastName();
                    String username = dailyLogs.getUser().getAccount().getUsername();
                    Date fromDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date + " 00:00:01");
                    Date toDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date + " 23:59:59");
                    List<ControlLogLcd> controlLogLcds = controlLogLcdRepository.
                            getControlLog(username, fromDate, toDate);
                    String departmentName = dailyLogs.getUser().getDepartment().getDepartmentName();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.US);
                    String dateDaily = sdf.format(Until.convertDateToCalender(dailyLogs.getDate()).getTime());
                    Time checkin = dailyLogs.getCheckin();
                    Time checkout = dailyLogs.getCheckout();
                    double totalAttendance = dailyLogs.getTotalAttendance();
                    double morningTotal = dailyLogs.getMorningTotal();
                    double afternoonTotal = dailyLogs.getAfternoonTotal();
                    boolean lateCheckin = dailyLogs.isLateCheckin();
                    boolean earlyCheckout = dailyLogs.isEarlyCheckout();
                    boolean nonPermittedLeave = dailyLogs.getNonPermittedLeave() > 0.0;
                    double permittedLeave = dailyLogs.getPermittedLeave();
                    double outsideWork = dailyLogs.getOutsideWork();
                    List<ControlLogResponse> controlLogResponse = new ArrayList<>();
                    ControlLogResponse controlLogResponse1 = new ControlLogResponse();
                    controlLogLcds.forEach(element -> {
                        controlLogResponse1.setLog(element.getTime().toString());
                        controlLogResponse1.setUsername(username);
                        controlLogResponse.add(controlLogResponse1);
                    });
                    return AttendanceDetailResponse.builder()
                            .name(name).username(username).departmentName(departmentName).dateDaily(dateDaily)
                            .checkin(checkin).checkout(checkout).totalAttendance(totalAttendance)
                            .morningTotal(morningTotal).afternoonTotal(afternoonTotal).lateCheckin(lateCheckin)
                            .earlyCheckout(earlyCheckout).permittedLeave(permittedLeave).nonPermittedLeave(nonPermittedLeave)
                            .outsideWork(outsideWork).controlLogResponse(controlLogResponse)
                            .build();
                } else {
                    throw new NotFound("request_fail");
                }
            } catch (ServerError | ParseException e) {
                throw new ServerError("fail");
            }
        } else {
            throw new BadRequest("request_fail");
        }
    }

    public int getCheckWeekend(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    @Transactional
    public void updateAttendanceTime(java.sql.Date date, User user, Time checkin, Time checkout) {
        DailyLog dailyLog = dailyLogRepository.findByUserAndDate(user, date)
                .orElseThrow(() -> new BadRequest("Not_found"));
        if (checkin != null) {
            dailyLog.setCheckin(checkin);
        }
        if (checkout != null) {
            dailyLog.setCheckout(checkout);
        }
        dailyLogService.updateDailyLog(user, date, checkin, checkout);
        try {
            dailyLogRepository.save(dailyLog);
        } catch (Exception e) {
            throw new ServerError("Something went wrong");
        }
    }
}
