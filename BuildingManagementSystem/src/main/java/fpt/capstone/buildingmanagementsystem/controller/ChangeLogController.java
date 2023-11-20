package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.request.SaveChangeLogRequest;
import fpt.capstone.buildingmanagementsystem.service.RequestChangeLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ChangeLogController {
    @Autowired
    RequestChangeLogService requestChangeLogService;

    @PostMapping("/saveChangeLog")
    public boolean saveChangeLog(@RequestBody SaveChangeLogRequest saveChangeLogRequest) {
        return requestChangeLogService.saveChangeLog(saveChangeLogRequest);
    }
}
