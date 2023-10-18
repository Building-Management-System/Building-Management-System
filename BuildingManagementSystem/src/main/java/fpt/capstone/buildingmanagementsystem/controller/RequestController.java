package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.request.*;
import fpt.capstone.buildingmanagementsystem.model.response.ReceiveIdAndDepartmentIdResponse;
import fpt.capstone.buildingmanagementsystem.service.RequestAttendanceFromService;
import fpt.capstone.buildingmanagementsystem.service.RequestLeaveFormService;
import fpt.capstone.buildingmanagementsystem.service.RequestOtherService;
import fpt.capstone.buildingmanagementsystem.service.UserManageService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class RequestController {
    @Autowired
    RequestOtherService requestOtherService;
    @Autowired
    RequestAttendanceFromService requestAttendanceFromService;
    @Autowired
    RequestLeaveFormService requestLeaveFormService;
    @Autowired
    UserManageService userManageService;
    @PostMapping("/getReceiveIdAndDepartmentId")
    public ReceiveIdAndDepartmentIdResponse getReceiveIdAndDepartmentId(@RequestBody GetUserInfoRequest getUserId) {
        return userManageService.getReceiveIdAndDepartmentId(getUserId.getUserId());
    }
    @PostMapping("/requestAttendanceForm")
    public boolean requestAttendanceForm(@RequestBody SendAttendanceFormRequest sendAttendanceFormRequest) {
        return requestAttendanceFromService.getAttendanceUser(sendAttendanceFormRequest);
    }
    @PostMapping("/requestLeaveForm")
    public boolean requestLeaveForm(@RequestBody SendLeaveFormRequest sendLeaveFormRequest) {
        return requestLeaveFormService.getLeaveFormUser(sendLeaveFormRequest);
    }
    @PostMapping("/otherForm")
    public boolean requestOtherForm(@RequestBody SendOtherFormRequest sendOtherFormRequest) {
        return requestOtherService.getLeaveFormUser(sendOtherFormRequest);
    }
}
