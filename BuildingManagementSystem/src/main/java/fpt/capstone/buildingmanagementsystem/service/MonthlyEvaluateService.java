package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.model.entity.DailyLog;
import fpt.capstone.buildingmanagementsystem.model.entity.MonthlyEvaluate;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.request.MonthlyEvaluateRequest;
import fpt.capstone.buildingmanagementsystem.model.response.MonthlyEvaluateResponse;
import fpt.capstone.buildingmanagementsystem.repository.AccountRepository;
import fpt.capstone.buildingmanagementsystem.repository.DailyLogRepository;
import fpt.capstone.buildingmanagementsystem.repository.MonthlyEvaluateRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonthlyEvaluateService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    DailyLogService dailyLogService;

    @Autowired
    DailyLogRepository dailyLogRepository;

    @Autowired
    MonthlyEvaluateRepository monthlyEvaluateRepository;

    public boolean getEvaluate(MonthlyEvaluateRequest request) {
        List<DailyLog> dailyLogs = dailyLogRepository.getMonthlyDailyLog(request.getUserId(), request.getMonth(), request.getYear());
        return true;
    }

    public MonthlyEvaluateResponse getMonthlyEvaluateOfEmployee(MonthlyEvaluateRequest request) {
        User user = userRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new BadRequest("Not_found_user"));
        MonthlyEvaluate monthlyEvaluate = monthlyEvaluateRepository.findByEmployeeAndMonthAndYear(user, request.getMonth(), request.getYear())
                .orElseThrow(() -> new BadRequest("Not_fount_report"));
        MonthlyEvaluateResponse response = new MonthlyEvaluateResponse();
        BeanUtils.copyProperties(monthlyEvaluate, response);
        response.setCreatedBy(monthlyEvaluate.getCreatedBy().getUserId());
        response.setEmployeeId(monthlyEvaluate.getEmployee().getUserId());
        response.setFirstNameEmp(monthlyEvaluate.getEmployee().getFirstName());
        response.setLastNameEmp(monthlyEvaluate.getEmployee().getLastName());
        response.setDepartment(monthlyEvaluate.getEmployee().getDepartment());
        response.setHireDate(monthlyEvaluate.getEmployee().getAccount().getCreatedDate());
        return response;
    }

    public List<MonthlyEvaluateResponse> getEvaluateOfDepartment(String departmentId, int month, int year) {
        List<MonthlyEvaluate> monthlyEvaluates = monthlyEvaluateRepository.findByDepartmentAndMonthAndYear(departmentId, month, year)
                .stream()
                .filter(monthlyEvaluate -> monthlyEvaluate.getEmployee()
                        .getAccount()
                        .getRole()
                        .getRoleName().equals("employee"))
                .collect(Collectors.toList());

        List<MonthlyEvaluateResponse> responses = new ArrayList<>();
        monthlyEvaluates.forEach(monthlyEvaluate -> {
            MonthlyEvaluateResponse response = new MonthlyEvaluateResponse();
            BeanUtils.copyProperties(monthlyEvaluate, response);
            response.setCreatedBy(monthlyEvaluate.getCreatedBy().getUserId());
            response.setEmployeeId(monthlyEvaluate.getEmployee().getUserId());
            response.setFirstNameEmp(monthlyEvaluate.getEmployee().getFirstName());
            response.setLastNameEmp(monthlyEvaluate.getEmployee().getLastName());
            response.setDepartment(monthlyEvaluate.getEmployee().getDepartment());
            response.setHireDate(monthlyEvaluate.getEmployee().getAccount().getCreatedDate());
            response.setEmployeeUserName(monthlyEvaluate.getEmployee().getAccount().getUsername());
            response.setAcceptedHrId(monthlyEvaluate.getAcceptedBy() == null ? null : monthlyEvaluate.getAcceptedBy().getUserId());
            response.setAcceptedHrUserName(monthlyEvaluate.getAcceptedBy() == null ? null :monthlyEvaluate.getAcceptedBy().getAccount().username);
            responses.add(response);
        });
        return responses;
    }

}
