package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.request.*;
import fpt.capstone.buildingmanagementsystem.model.response.ReceiveIdAndDepartmentIdResponse;
import fpt.capstone.buildingmanagementsystem.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    @Autowired
    RoomBookingService roomBookingService;
    @Autowired
    RequestOvertimeFormService requestOvertimeFormService;
    @PostMapping("/getReceiveIdAndDepartmentId")
    public ReceiveIdAndDepartmentIdResponse getReceiveIdAndDepartmentId(@RequestBody GetUserInfoRequest getUserId) {
        return userManageService.getReceiveIdAndDepartmentId(getUserId.getUserId());
    }
    @PostMapping("/requestAttendanceForm")
    public boolean requestAttendanceForm(@RequestBody SendAttendanceFormRequest sendAttendanceFormRequest) {
        return requestAttendanceFromService.getAttendanceUser(sendAttendanceFormRequest);
    }
    @PostMapping("/requestAttendanceFormExistTicket")
    public boolean requestAttendanceFormExistTicket(@RequestBody SendAttendanceFormRequest sendAttendanceFormRequest) {
        return requestAttendanceFromService.getAttendanceUserExistTicket(sendAttendanceFormRequest);
    }
    @PostMapping("/requestAttendanceFormExistRequest")
    public boolean requestAttendanceFormExistRequest(@RequestBody SendAttendanceFormRequest sendAttendanceFormRequest) {
        return requestAttendanceFromService.getAttendanceUserExistRequest(sendAttendanceFormRequest);
    }
    @PostMapping("/requestLeaveForm")
    public boolean requestLeaveForm(@RequestBody SendLeaveFormRequest sendLeaveFormRequest) {
        return requestLeaveFormService.getLeaveFormUser(sendLeaveFormRequest);
    }
    @PostMapping("/requestLeaveFormExistTicket")
    public boolean requestLeaveFormUserExistTicket(@RequestBody SendLeaveFormRequest sendLeaveFormRequest) {
        return requestLeaveFormService.getLeaveFormUserExistTicket(sendLeaveFormRequest);
    }
    @PostMapping("/requestLeaveFormExistRequest")
    public boolean requestLeaveFormUserExistRequest(@RequestBody SendLeaveFormRequest sendLeaveFormRequest) {
        return requestLeaveFormService.getLeaveFormUserExistRequest(sendLeaveFormRequest);
    }

    @PostMapping("/roomBookingForm")
    public boolean requestRoomBookingForm(@RequestBody SendRoomBookingRequest sendRoomBookingRequest) {
        return roomBookingService.getRoomBookingForm(sendRoomBookingRequest);
    }
    @PostMapping("/roomBookingFormExistTicket")
    public boolean requestRoomBookingFormExistTicket(@RequestBody SendRoomBookingRequest sendRoomBookingRequest) {
        return roomBookingService.getRoomBookingFormExistTicket(sendRoomBookingRequest);
    }
    @PostMapping("/roomBookingFormExistRequest")
    public boolean requestRoomBookingFormExistRequest(@RequestBody SendRoomBookingRequest sendRoomBookingRequest) {
        return roomBookingService.getRoomBookingFormExistRequest(sendRoomBookingRequest);
    }
    @PostMapping("/otherForm")
    public boolean requestOtherForm(@RequestBody SendOtherFormRequest sendOtherFormRequest) {
        return requestOtherService.getOtherFormUser(sendOtherFormRequest);
    }
    @PostMapping("/otherFormExistTicket")
    public boolean requestOtherFormExistTicket(@RequestBody SendOtherFormRequest sendOtherFormRequest) {
        return requestOtherService.getOtherFormUserExistTicket(sendOtherFormRequest);
    }
    @PostMapping("/otherFormExistRequest")
    public boolean requestOtherFormExistRequest(@RequestBody SendOtherFormRequest sendOtherFormRequest) {
        return requestOtherService.getOtherFormUserExistRequest(sendOtherFormRequest);
    }
    @PostMapping("/overTimeForm")
    public boolean requestOverTimeForm(@RequestBody SendOvertimeFormRequest sendOvertimeFormRequest) {
        return requestOvertimeFormService.getOvertimeFormUser(sendOvertimeFormRequest);
    }
    @PostMapping("/overTimeFormExistTicket")
    public boolean requestOverTimeExistTicket(@RequestBody SendOvertimeFormRequest sendOvertimeFormRequest) {
        return requestOvertimeFormService.getOvertimeExistTicket(sendOvertimeFormRequest);
    }
    @PostMapping("/overTimeFormExistRequest")
    public boolean requestOverTimeExistRequest(@RequestBody SendOvertimeFormRequest sendOvertimeFormRequest) {
        return requestOvertimeFormService.getOvertimeExistRequest(sendOvertimeFormRequest);
    }
}
