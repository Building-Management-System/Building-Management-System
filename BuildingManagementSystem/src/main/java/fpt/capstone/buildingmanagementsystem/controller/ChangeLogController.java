package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.request.ChangeLogRequest;
import fpt.capstone.buildingmanagementsystem.model.request.SaveChangeLogRequest;
import fpt.capstone.buildingmanagementsystem.model.response.ChangeLogResponse;
import fpt.capstone.buildingmanagementsystem.service.ChangeLogService;
import fpt.capstone.buildingmanagementsystem.service.RequestChangeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class ChangeLogController {
    @Autowired
    RequestChangeLogService requestChangeLogService;

    @Autowired
    ChangeLogService changeLogService;

    @PostMapping("/saveChangeLog")
    public boolean saveChangeLog(@RequestBody SaveChangeLogRequest saveChangeLogRequest) {
        return requestChangeLogService.saveChangeLog(saveChangeLogRequest);
    }

    @GetMapping("getChangeLogByEmployeeAndMonth")
    public List<ChangeLogResponse> getChangeLogByEmployeeAndMonth(@RequestBody ChangeLogRequest changeLogRequest) {
        return changeLogService.getAllChangeLogByEmployeeIdAndMonth(changeLogRequest);
    }
}
