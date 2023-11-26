package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.Conflict;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.model.entity.DailyLog;
import fpt.capstone.buildingmanagementsystem.model.entity.MonthlyEvaluate;
import fpt.capstone.buildingmanagementsystem.model.entity.OvertimeLog;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.request.EmployeeEvaluateRequest;
import fpt.capstone.buildingmanagementsystem.model.request.MonthlyEvaluateRequest;
import fpt.capstone.buildingmanagementsystem.model.response.EmployeeResponse;
import fpt.capstone.buildingmanagementsystem.model.response.MonthlyEvaluateResponse;
import fpt.capstone.buildingmanagementsystem.repository.AccountRepository;
import fpt.capstone.buildingmanagementsystem.repository.DailyLogRepository;
import fpt.capstone.buildingmanagementsystem.repository.MonthlyEvaluateRepository;
import fpt.capstone.buildingmanagementsystem.repository.OverTimeRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import fpt.capstone.buildingmanagementsystem.until.Until;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    OverTimeRepository overTimeRepository;

    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<?> createEvaluate(EmployeeEvaluateRequest request) {
        User employee = userRepository.findByUserId(request.getEmployeeId())
                .orElseThrow(() -> new BadRequest("Not_found_user"));

        User manager = userRepository.findByUserId(request.getManagerId())
                .orElseThrow(() -> new BadRequest("Not_found_manager"));

        Optional<MonthlyEvaluate> monthlyEvaluateOptional = monthlyEvaluateRepository.findByEmployeeAndMonthAndYear(employee, request.getMonth(), request.getYear());

        if (monthlyEvaluateOptional.isPresent()) throw new Conflict("Evaluate_existed");
        // daily log
        try {
            double totalAttendance = 0.0;
            double morningTotal = 0.0;
            double afternoonTotal = 0.0;
            int lateCheckinTotal = 0;
            int earlyCheckoutTotal = 0;
            double permittedLeave = 0.0;
            double nonPermittedLeave = 0.0;
            int ViolateTotal = 0;
            double outsideWork = 0.0;
            double paidDay = 0.0;
            int totalDay = 0;
            List<DailyLog> dailyLogs = dailyLogRepository.getByUserIdAndMonthAndYear(request.getEmployeeId(), request.getMonth(), request.getYear() + "");
            dailyLogs = dailyLogs.stream()
                    .sorted((Comparator.comparing(DailyLog::getDate).reversed()))
                    .collect(Collectors.toList());
            if (dailyLogs.size() > 0) {
                for (DailyLog dailyLog : dailyLogs) {
                    totalAttendance = totalAttendance + dailyLog.getTotalAttendance();
                    morningTotal = morningTotal + dailyLog.getMorningTotal();
                    afternoonTotal = afternoonTotal + dailyLog.getAfternoonTotal();
                    if (dailyLog.isEarlyCheckout()) {
                        lateCheckinTotal = lateCheckinTotal + 1;
                    }
                    if (dailyLog.isEarlyCheckout()) {
                        earlyCheckoutTotal = earlyCheckoutTotal + 1;
                    }
                    if (dailyLog.isViolate()) {
                        ViolateTotal = ViolateTotal + 1;
                    }
                    if ((getCheckWeekend(dailyLog.getDate()) != Calendar.SATURDAY) && (getCheckWeekend(dailyLog.getDate()) != Calendar.SUNDAY)) {
                        totalDay = totalDay + 1;
                    }
                    permittedLeave = permittedLeave + dailyLog.getPermittedLeave();
                    nonPermittedLeave = nonPermittedLeave + dailyLog.getNonPermittedLeave();
                    outsideWork = outsideWork + dailyLog.getOutsideWork();
                    paidDay = paidDay + dailyLog.getPaidDay();
                }

            } else {
                throw new NotFound("list_null");
            }
            // overtime log
            List<OvertimeLog> overtimeLogs = overTimeRepository.findByUserAndMonthAndYear(request.getEmployeeId(), request.getYear(), request.getMonth());
            double overtimeTotal = 0;
            if (!overtimeLogs.isEmpty()) {
                overtimeTotal = overtimeLogs.stream()
                        .mapToDouble(OvertimeLog::getTotalPaid)
                        .sum();
            }

            MonthlyEvaluate monthlyEvaluate = MonthlyEvaluate.builder()
                    .workingDay(totalDay)
                    .totalAttendance(totalAttendance)
                    .lateCheckin(lateCheckinTotal)
                    .earlyCheckout(earlyCheckoutTotal)
                    .permittedLeave(permittedLeave)
                    .nonPermittedLeave(nonPermittedLeave)
                    .violate(ViolateTotal)
                    .paidDay(paidDay)
                    .workingOutside(outsideWork)
                    .overTime(overtimeTotal)
                    .month(request.getMonth())
                    .year(request.getYear())
                    .evaluateEnum(request.getRate())
                    .note(request.getNote())
                    .createdDate(Until.generateDate())
                    .updateDate(Until.generateDate())
                    .status(false)
                    .createdBy(manager)
                    .employee(employee)
                    .build();
            try {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(monthlyEvaluateRepository.save(monthlyEvaluate));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new EmployeeResponse());
            }
        } catch (ServerError e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new EmployeeResponse());
        }
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
            response.setUsernameCreatedBy(monthlyEvaluate.getCreatedBy().getAccount().getUsername());
            response.setEmployeeId(monthlyEvaluate.getEmployee().getUserId());
            response.setFirstNameEmp(monthlyEvaluate.getEmployee().getFirstName());
            response.setLastNameEmp(monthlyEvaluate.getEmployee().getLastName());
            response.setDepartment(monthlyEvaluate.getEmployee().getDepartment());
            response.setHireDate(monthlyEvaluate.getEmployee().getAccount().getCreatedDate());
            response.setEmployeeUserName(monthlyEvaluate.getEmployee().getAccount().getUsername());
            response.setAcceptedHrId(monthlyEvaluate.getAcceptedBy() == null ? null : monthlyEvaluate.getAcceptedBy().getUserId());
            response.setAcceptedHrUserName(monthlyEvaluate.getAcceptedBy() == null ? null : monthlyEvaluate.getAcceptedBy().getAccount().username);
            responses.add(response);
        });
        return responses;
    }

    public int getCheckWeekend(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

}
