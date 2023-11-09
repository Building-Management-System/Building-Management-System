package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.request.LateMessageRequest;
import fpt.capstone.buildingmanagementsystem.service.LateRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class LateRequestController {
    @Autowired
    LateRequestService lateRequestService;

    @PostMapping("acceptLateRequest")
    public boolean acceptLateRequest(@RequestParam("lateRequestId") String lateRequestId) {
        return lateRequestService.acceptLateRequest(lateRequestId);
    }

    @PostMapping("rejectLateRequest")
    public boolean rejectLateRequest(@RequestBody LateMessageRequest lateMessageRequest) {
        return lateRequestService.rejectLateRequest(lateMessageRequest);
    }
}
