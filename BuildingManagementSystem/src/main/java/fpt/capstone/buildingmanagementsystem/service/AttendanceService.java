package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.mapper.DailyLogMapper;
import fpt.capstone.buildingmanagementsystem.mapper.OvertimeLogMapper;
import fpt.capstone.buildingmanagementsystem.model.entity.DailyLog;
import fpt.capstone.buildingmanagementsystem.model.entity.OvertimeLog;
import fpt.capstone.buildingmanagementsystem.model.response.AttendanceDetailResponse;
import fpt.capstone.buildingmanagementsystem.model.response.DailyDetailResponse;
import fpt.capstone.buildingmanagementsystem.model.response.GetAttendanceUserResponse;
import fpt.capstone.buildingmanagementsystem.model.response.OverTimeDetailResponse;
import fpt.capstone.buildingmanagementsystem.repository.DailyLogRepository;
import fpt.capstone.buildingmanagementsystem.repository.OverTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;


@Service
public class AttendanceService {
    @Autowired
    DailyLogRepository dailyLogRepository;
    @Autowired
    DailyLogMapper dailyLogMapper;
    @Autowired
    OvertimeLogMapper overtimeLogMapper;
    @Autowired
    OverTimeRepository overTimeRepository;

    public List<GetAttendanceUserResponse> getAttendanceUser(String user_id) {
        try {
            if (user_id != null) {
                List<DailyLog> dailyLogs = dailyLogRepository.getAttendanceUser(user_id);
                List<GetAttendanceUserResponse> getAttendanceUserResponseList = dailyLogMapper.convertGetAttendanceUserResponse(dailyLogs);
                return getAttendanceUserResponseList;
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (ServerError e) {
            throw new ServerError("fail");
        }
    }

    public AttendanceDetailResponse getAttendanceDetail(String user_id, String date) {
        if (user_id != null) {
            try {
                DailyLog dailyLogs = dailyLogRepository.getAttendanceDetail(user_id, date);
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
}
