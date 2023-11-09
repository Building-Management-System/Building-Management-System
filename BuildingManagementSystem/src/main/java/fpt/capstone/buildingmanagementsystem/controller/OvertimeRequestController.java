package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.request.OvertimeMessageRequest;
import fpt.capstone.buildingmanagementsystem.service.OvertimeRequestFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class OvertimeRequestController {

    @Autowired
    OvertimeRequestFormService overtimeRequestFormService;

    @PostMapping("acceptOvertimeRequest")
    public boolean acceptOvertimeRequest(@RequestParam("overtimeRequestId") String overtimeRequestId) {
        return overtimeRequestFormService.acceptOvertimeRequest(overtimeRequestId);
    }

    @PostMapping("rejectOvertimeRequest")
    public boolean rejectOvertimeRequest(@RequestBody OvertimeMessageRequest overtimeMessageRequest) {
        return overtimeRequestFormService.rejectOvertimeRequest(overtimeMessageRequest);
    }
 }
