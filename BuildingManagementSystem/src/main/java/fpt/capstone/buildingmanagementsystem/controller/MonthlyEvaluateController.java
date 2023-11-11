package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.request.MonthlyEvaluateRequest;
import fpt.capstone.buildingmanagementsystem.model.response.MonthlyEvaluateResponse;
import fpt.capstone.buildingmanagementsystem.service.MonthlyEvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class MonthlyEvaluateController {

    @Autowired
    MonthlyEvaluateService monthlyEvaluateService;

    @GetMapping("/getEvaluate")
    public MonthlyEvaluateResponse getEvaluate(@RequestBody MonthlyEvaluateRequest monthlyEvaluateRequest) {
        return monthlyEvaluateService.getMonthlyEvaluate(monthlyEvaluateRequest);
    }

}
