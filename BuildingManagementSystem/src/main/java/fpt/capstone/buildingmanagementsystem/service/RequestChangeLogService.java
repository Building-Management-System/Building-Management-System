package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.Conflict;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.mapper.ChangeLogMapper;
import fpt.capstone.buildingmanagementsystem.model.entity.ChangeLog;
import fpt.capstone.buildingmanagementsystem.model.entity.ControlLogLcd;
import fpt.capstone.buildingmanagementsystem.model.entity.DailyLog;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.request.SaveChangeLogRequest;
import fpt.capstone.buildingmanagementsystem.model.response.AttendanceDetailResponse;
import fpt.capstone.buildingmanagementsystem.model.response.ChangeLogDetailResponse;
import fpt.capstone.buildingmanagementsystem.model.response.ControlLogResponse;
import fpt.capstone.buildingmanagementsystem.repository.ChangeLogRepository;
import fpt.capstone.buildingmanagementsystem.repository.ControlLogLcdRepository;
import fpt.capstone.buildingmanagementsystem.repository.DailyLogRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import fpt.capstone.buildingmanagementsystem.until.Until;
import fpt.capstone.buildingmanagementsystem.validate.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static fpt.capstone.buildingmanagementsystem.validate.Validate.validateStartTimeAndEndTime;

@Service
public class RequestChangeLogService {
    @Autowired
    ControlLogLcdRepository controlLogLcdRepository;
    @Autowired
    DailyLogRepository dailyLogRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChangeLogRepository changeLogRepository;
    @Autowired
    ChangeLogMapper changeLogMapper;
    public boolean saveChangeLog(SaveChangeLogRequest saveChangeLogRequest) {
        try {
            if (saveChangeLogRequest.getManagerId() != null &&
                    saveChangeLogRequest.getEmployeeId() != null &&
                    saveChangeLogRequest.getDate() != null &&
                    saveChangeLogRequest.getChangeType() != null
            ) {
                ChangeLog changeLog = changeLogMapper.convert(saveChangeLogRequest);
                if (validateStartTimeAndEndTime(saveChangeLogRequest.getManualCheckIn(), saveChangeLogRequest.getManualCheckOut())) {
                    Optional<User> userEmp = userRepository.findByUserId(saveChangeLogRequest.getEmployeeId());
                    Optional<User> userManager = userRepository.findByUserId(saveChangeLogRequest.getManagerId());
                    if (userEmp.isPresent() && userManager.isPresent()) {
                        if (saveChangeLogRequest.getWorkOutSide() == null) changeLog.setOutsideWork(-1);
                        changeLog.setEmployee(userEmp.get());
                        changeLog.setManager(userManager.get());
                        Optional<ChangeLog> optionalChangeLog=changeLogRepository.getChangeLogDetailByUserIdAndDate(saveChangeLogRequest.getEmployeeId(),saveChangeLogRequest.getDate());
                        if(optionalChangeLog.isPresent()) {
                            changeLog.setChangeLogId(optionalChangeLog.get().getChangeLogId());
                            changeLogRepository.save(changeLog);
                            return true;
                        }else {
                            changeLogRepository.save(changeLog);
                            return true;
                        }
                    } else {
                        throw new BadRequest("request_fail");
                    }
                } else {
                    throw new Conflict("checkout_time_need_to_be_larger_checkin");
                }
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (ServerError | ParseException e) {
            throw new ServerError("fail");
        }
    }
    public ChangeLogDetailResponse getChangeLogDetail(String employee_id, String date) {
        if (employee_id != null && Validate.validateDateFormat(date)) {
            try {
                Optional<DailyLog> dailyLogOptional = dailyLogRepository.getAttendanceDetailByUserIdAndDate(employee_id, date);
                if (dailyLogOptional.isPresent()) {
                    DailyLog dailyLogs = dailyLogOptional.get();
                    String name = dailyLogs.getUser().getFirstName() + " " + dailyLogs.getUser().getLastName();
                    String username = dailyLogs.getUser().getAccount().getUsername();
                    List<ControlLogLcd> controlLogLcds = controlLogLcdRepository.getControlLogLcdList(username, date);
                    controlLogLcds=controlLogLcds.stream()
                            .sorted((Comparator.comparing(ControlLogLcd::getTime)))
                            .collect(Collectors.toList());
                    String departmentName = dailyLogs.getUser().getDepartment().getDepartmentName();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.US);
                    String dateDaily = sdf.format(Until.convertDateToCalender(dailyLogs.getDate()).getTime());
                    Time checkin = dailyLogs.getCheckin();
                    Time checkout = dailyLogs.getCheckout();
                    Optional<ChangeLog> changeLog=changeLogRepository.getChangeLogDetailByUserIdAndDate(employee_id,date);
                    Time checkinChange = null;
                    Time checkoutChange = null;
                    String dateDailyChange = null;
                    String changeFrom = null;
                    boolean violate = false;
                    double outSideWork = 0;
                    String reason=null;
                    if(changeLog.isPresent()) {
                         checkinChange = changeLog.get().getCheckin();
                         checkoutChange = changeLog.get().getCheckout();
                         dateDailyChange = sdf.format(Until.convertDateToCalender(changeLog.get().getCreatedDate()).getTime());
                         changeFrom = changeLog.get().getManager().getAccount().getUsername();
                         violate = changeLog.get().isViolate();
                         outSideWork = changeLog.get().getOutsideWork();
                         reason=changeLog.get().getReason();
                    }
                    List<ControlLogResponse> controlLogResponse = new ArrayList<>();
                    controlLogLcds.forEach(element -> {
                        ControlLogResponse controlLogResponse1 = new ControlLogResponse();
                        controlLogResponse1.setLog(element.getTime().toString());
                        controlLogResponse1.setUsername(username);
                        controlLogResponse.add(controlLogResponse1);
                    });
                    return ChangeLogDetailResponse.builder()
                            .name(name).username(username).departmentName(departmentName).dateDaily(dateDaily)
                            .checkin(checkin).checkout(checkout).checkinChange(checkinChange).checkoutChange(checkoutChange).dateDailyChange(dateDailyChange)
                            .violate(violate).changeFrom(changeFrom).outSideWork(outSideWork).reason(reason)
                            .controlLogResponse(controlLogResponse)
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
}
