package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.request.SendAttendanceFormRequest;
import fpt.capstone.buildingmanagementsystem.model.request.SendLeaveFormRequest;
import fpt.capstone.buildingmanagementsystem.service.RequestAttendanceFromService;
import fpt.capstone.buildingmanagementsystem.service.RequestLeaveFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class RequestController {
    @Autowired
    RequestAttendanceFromService requestAttendanceFromService;
    @Autowired
    RequestLeaveFormService requestLeaveFormService;
    @PostMapping("/requestAttendanceForm")
    public boolean requestAttendanceForm(@RequestBody SendAttendanceFormRequest sendAttendanceFormRequest) {
        return requestAttendanceFromService.getAttendanceUser(sendAttendanceFormRequest);
    }
    @PostMapping("/requestLeaveForm")
    public boolean requestLeaveForm(@RequestBody SendLeaveFormRequest sendLeaveFormRequest) {
        return requestLeaveFormService.getLeaveFormUser(sendLeaveFormRequest);
    }
}
