package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.model.entity.*;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.DateType;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.EvaluateEnum;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.TopicOvertime;
import fpt.capstone.buildingmanagementsystem.model.request.EditEvaluateRequest;
import fpt.capstone.buildingmanagementsystem.model.request.EmployeeEvaluateRequest;
import fpt.capstone.buildingmanagementsystem.model.request.EvaluateByHrRequest;
import fpt.capstone.buildingmanagementsystem.model.request.MonthlyEvaluateRequest;
import fpt.capstone.buildingmanagementsystem.model.response.EmployeeEvaluateRemainResponse;
import fpt.capstone.buildingmanagementsystem.model.response.MonthlyEvaluateResponse;
import fpt.capstone.buildingmanagementsystem.model.response.MonthlyEvaluateSummaryResponse;
import fpt.capstone.buildingmanagementsystem.model.response.NotificationAcceptResponse;
import fpt.capstone.buildingmanagementsystem.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.mockito.Mockito.*;

class MonthlyEvaluateServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    AccountRepository accountRepository;
    @Mock
    DailyLogService dailyLogService;
    @Mock
    DailyLogRepository dailyLogRepository;
    @Mock
    MonthlyEvaluateRepository monthlyEvaluateRepository;
    @Mock
    OverTimeRepository overTimeRepository;
    @Mock
    DepartmentRepository departmentRepository;
    @Mock
    TicketManageService ticketManageService;
    @Mock
    AutomaticNotificationService automaticNotificationService;
    @InjectMocks
    MonthlyEvaluateService monthlyEvaluateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEvaluate() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        when(dailyLogRepository.getByUserIdAndMonthAndYear(anyString(), anyInt(), anyString())).thenReturn(List.of(new DailyLog("dailyId", null, 0, null, null, 0d, 0d, 0d, true, true, 0d, 0d, true, 0d, 0d, null, null, DateType.NORMAL, "description", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), new Department("departmentId", "departmentName")))));
        when(monthlyEvaluateRepository.findByEmployeeAndMonthAndYear(any(), anyInt(), anyInt())).thenReturn(null);
        when(overTimeRepository.findByUserAndMonthAndYear(anyString(), anyInt(), anyInt())).thenReturn(List.of(new OvertimeLog("otId", null, null, null, TopicOvertime.WEEKEND_AND_NORMAL_DAY, null, null, null, 0d, "description", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), null, null))));

        ResponseEntity<?> result = monthlyEvaluateService.createEvaluate(new EmployeeEvaluateRequest("employeeId", "managerId", 0, 0, "note", EvaluateEnum.GOOD));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testUpdateEvaluate() {
        ResponseEntity<?> result = monthlyEvaluateService.updateEvaluate(new EditEvaluateRequest("evaluateId", "note", EvaluateEnum.GOOD));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testUpdateAcceptOrRejectEvaluateByHr() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        when(monthlyEvaluateRepository.findByEvaluateId(anyString())).thenReturn(null);
        when(automaticNotificationService.sendApprovalEvaluateRequest(any())).thenReturn(new NotificationAcceptResponse("notificationId", "userId", "receiverId", "senderName", true, "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), null));

        NotificationAcceptResponse result = monthlyEvaluateService.updateAcceptOrRejectEvaluateByHr(new EvaluateByHrRequest("hrId", "hrNote", "evaluateId", "hrStatus"));
        Assertions.assertEquals(new NotificationAcceptResponse("notificationId", "userId", "receiverId", "senderName", true, "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), null), result);
    }

    @Test
    void testGetMonthlyEvaluateOfEmployee() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        when(monthlyEvaluateRepository.findByEmployeeAndMonthAndYear(any(), anyInt(), anyInt())).thenReturn(null);

        MonthlyEvaluateResponse result = monthlyEvaluateService.getMonthlyEvaluateOfEmployee(new MonthlyEvaluateRequest("userId", 0, 0));
        Assertions.assertEquals(new MonthlyEvaluateResponse("evaluateId", 0d, 0d, 0, 0, 0d, 0d, 0, 0d, 0d, 0, 0, EvaluateEnum.GOOD, "note", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), "createdBy", "UsernameCreatedBy", "employeeId", "employeeUserName", "firstNameEmp", "lastNameEmp", new Department("departmentId", "departmentName"), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), true, "acceptedHrId", "acceptedHrUserName", "hrNote"), result);
    }

    @Test
    void testGetEvaluateOfDepartment() {
        when(monthlyEvaluateRepository.findByDepartmentAndMonthAndYear(anyString(), anyInt(), anyInt())).thenReturn(List.of(new MonthlyEvaluate("evaluateId", 0d, 0d, 0, 0, 0d, 0d, 0, 0d, 0d, 0, 0, 0d, EvaluateEnum.GOOD, "note", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), true, "HrNote", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), null, null), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), null, null), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), null, null), null)));

        List<MonthlyEvaluateResponse> result = monthlyEvaluateService.getEvaluateOfDepartment("departmentId", 0, 0);
        Assertions.assertEquals(List.of(new MonthlyEvaluateResponse("evaluateId", 0d, 0d, 0, 0, 0d, 0d, 0, 0d, 0d, 0, 0, EvaluateEnum.GOOD, "note", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), "createdBy", "UsernameCreatedBy", "employeeId", "employeeUserName", "firstNameEmp", "lastNameEmp", new Department("departmentId", "departmentName"), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), true, "acceptedHrId", "acceptedHrUserName", "hrNote")), result);
    }

    @Test
    void testGetAllEvaluateOfEmployee() {
        when(monthlyEvaluateRepository.findByEmployee(any())).thenReturn(List.of(new MonthlyEvaluate("evaluateId", 0d, 0d, 0, 0, 0d, 0d, 0, 0d, 0d, 0, 0, 0d, EvaluateEnum.GOOD, "note", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), true, "HrNote", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), null, null), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), null, null), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), null, null), null)));

        List<MonthlyEvaluateSummaryResponse> result = monthlyEvaluateService.getAllEvaluateOfEmployee("employeeId");
        Assertions.assertEquals(List.of(new MonthlyEvaluateSummaryResponse("evaluateId", "employeeId", "employeeUserName", "employeeFirstName", "employeeLastName", new Department("departmentId", "departmentName"), 0d, 0, 0, EvaluateEnum.GOOD, new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime())), result);
    }

    @Test
    void testGetCheckWeekend() {
        int result = monthlyEvaluateService.getCheckWeekend(new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime());
        Assertions.assertEquals(0, result);
    }

    @Test
    void testCheckEvaluateExisted() {
        when(monthlyEvaluateRepository.findByEmployeeAndMonthAndYear(any(), anyInt(), anyInt())).thenReturn(null);

        boolean result = monthlyEvaluateService.checkEvaluateExisted("employeeId", 0, 0);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testEvaluateRemain() {
        when(userRepository.findAllByDepartment(any())).thenReturn(List.of(new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), null, null)));
        when(monthlyEvaluateRepository.findByDepartmentAndMonthAndYear(anyString(), anyInt(), anyInt())).thenReturn(List.of(new MonthlyEvaluate("evaluateId", 0d, 0d, 0, 0, 0d, 0d, 0, 0d, 0d, 0, 0, 0d, EvaluateEnum.GOOD, "note", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), true, "HrNote", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), null, null), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), null, null), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), null, null), null)));

        List<EmployeeEvaluateRemainResponse> result = monthlyEvaluateService.evaluateRemain("departmentId", 0, 0);
        Assertions.assertEquals(List.of(new EmployeeEvaluateRemainResponse("employeeId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 43).getTime(), "userName", 0, 0)), result);
    }
}
