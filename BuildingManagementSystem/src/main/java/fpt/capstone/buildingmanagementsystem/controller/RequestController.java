package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.request.SendAttendanceFormRequest;
import fpt.capstone.buildingmanagementsystem.service.RequestAttendanceFromService;
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
    @PostMapping("/requestAttendanceForm")
    public boolean requestAttendanceForm(@RequestBody SendAttendanceFormRequest sendAttendanceFormRequest) {
        return requestAttendanceFromService.getAttendanceUser(sendAttendanceFormRequest);
    }
}
