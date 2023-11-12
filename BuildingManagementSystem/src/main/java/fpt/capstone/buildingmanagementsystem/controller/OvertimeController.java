package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.response.GetOvertimeListResponse;
import fpt.capstone.buildingmanagementsystem.service.OvertimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class OvertimeController {
    @Autowired
    OvertimeService overtimeService;
    @GetMapping("/getOvertimeListUser")
    public GetOvertimeListResponse getOvertimeListUser(@Param("user_id") String user_id, @Param("month") String month, @Param("month") String year) {
        return overtimeService.getOvertime(user_id,month,year);
    }
}
