package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.response.AttendanceDetailResponse;
import fpt.capstone.buildingmanagementsystem.model.response.GetAttendanceUserResponse;
import fpt.capstone.buildingmanagementsystem.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.List;

@RestController
@CrossOrigin
public class AttendanceController {
    @Autowired
    AttendanceService attendanceService;
    @GetMapping("/getAttendanceUser")
    public List<GetAttendanceUserResponse> getAttendanceUser(@Param("user_id") String user_id) {
        return attendanceService.getAttendanceUser(user_id);
    }
    @GetMapping("/getAttendanceUserDetail")
    public AttendanceDetailResponse getAttendanceUserDetail(@Param("user_id") String user_id, @Param("date") String date) {
        return attendanceService.getAttendanceDetail(user_id,date);
    }
}
