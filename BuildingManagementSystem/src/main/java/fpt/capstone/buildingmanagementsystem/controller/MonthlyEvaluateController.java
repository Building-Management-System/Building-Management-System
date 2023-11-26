package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.request.EditEvaluateRequest;
import fpt.capstone.buildingmanagementsystem.model.request.EmployeeEvaluateRequest;
import fpt.capstone.buildingmanagementsystem.model.request.MonthlyEvaluateRequest;
import fpt.capstone.buildingmanagementsystem.model.response.MonthlyEvaluateResponse;
import fpt.capstone.buildingmanagementsystem.service.MonthlyEvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class MonthlyEvaluateController {

    @Autowired
    MonthlyEvaluateService monthlyEvaluateService;

    @PostMapping("/createEvaluate")
    public ResponseEntity<?> createEmployeeEvaluate(@RequestBody EmployeeEvaluateRequest request) {
        return monthlyEvaluateService.createEvaluate(request);
    }

    @PostMapping("/getIndividualEvaluate")
    public MonthlyEvaluateResponse getEvaluate(@RequestBody MonthlyEvaluateRequest monthlyEvaluateRequest) {
        return monthlyEvaluateService.getMonthlyEvaluateOfEmployee(monthlyEvaluateRequest);
    }

    @PostMapping("/updateEvaluateRecord")
    public ResponseEntity<?> updateEvaluate(@RequestBody EditEvaluateRequest request) {
        return monthlyEvaluateService.updateEvaluate(request);
    }

    @GetMapping("/getDepartmentEvaluate")
    public List<MonthlyEvaluateResponse> getDepartmentEvaluate(@RequestParam("department_id") String departmentId,
                                                               @RequestParam("month") int month,
                                                               @RequestParam("year") int year) {
        return monthlyEvaluateService.getEvaluateOfDepartment(departmentId, month, year);
    }

}
