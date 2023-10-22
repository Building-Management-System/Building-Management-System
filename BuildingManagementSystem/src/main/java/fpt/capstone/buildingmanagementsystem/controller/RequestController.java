package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.request.GetUserInfoRequest;
import fpt.capstone.buildingmanagementsystem.model.request.SendAttendanceFormRequest;
import fpt.capstone.buildingmanagementsystem.model.request.SendLeaveFormRequest;
import fpt.capstone.buildingmanagementsystem.model.request.SendOtherFormRequest;
import fpt.capstone.buildingmanagementsystem.model.request.SendRoomBookingRequest;
import fpt.capstone.buildingmanagementsystem.model.response.ReceiveIdAndDepartmentIdResponse;
import fpt.capstone.buildingmanagementsystem.service.RequestAttendanceFromService;
import fpt.capstone.buildingmanagementsystem.service.RequestLeaveFormService;
import fpt.capstone.buildingmanagementsystem.service.RequestOtherService;
import fpt.capstone.buildingmanagementsystem.service.RoomBookingService;
import fpt.capstone.buildingmanagementsystem.service.UserManageService;
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
}
