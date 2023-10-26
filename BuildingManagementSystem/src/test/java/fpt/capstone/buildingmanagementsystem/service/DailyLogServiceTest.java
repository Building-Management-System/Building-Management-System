package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.model.entity.*;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.ControlLogStatus;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.DateType;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.DeviceStatus;
import fpt.capstone.buildingmanagementsystem.model.response.LateFormResponse;
import fpt.capstone.buildingmanagementsystem.repository.*;
import fpt.capstone.buildingmanagementsystem.service.schedule.CheckoutAnalyzeSchedule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.mockito.Mockito.*;

class DailyLogServiceTest {
    @Mock
    AccountRepository accountRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    DailyLogRepository dailyLogRepository;
    @Mock
    LateRequestFormRepositoryV2 lateRequestFormRepositoryV2;
    @Mock
    CheckoutAnalyzeSchedule checkoutAnalyzeSchedule;
    @Mock
    DayOffRepository dayOffRepository;
    @Mock
    HolidayService holidayService;
    @Mock
    Time startMorningTime;
    @Mock
    Time endMorningTime;
    @Mock
    Time startAfternoonTime;
    @Mock
    Logger logger;
    @InjectMocks
    DailyLogService dailyLogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMapControlLogToDailyLog() {
        when(accountRepository.findByUsername(anyString())).thenReturn(null);
        when(dailyLogRepository.findByUserAndDate(any(), any())).thenReturn(null);
        when(lateRequestFormRepositoryV2.findLateAndEarlyViolateByUserIdAndDate(anyString(), any(), any())).thenReturn(List.of(new LateFormResponse("userId", 0, null, "lateType")));
        when(holidayService.changeDailyLogToHoliday(any())).thenReturn(true);

        dailyLogService.mapControlLogToDailyLog(new ControlLogLcd("controlLogId", "operator", "personId", ControlLogStatus.WHITE_LIST, 0, 0, 0d, 0d, "persionName", "telnum", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), 0d, 0d, new byte[]{(byte) 0}, new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), "createdBy", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), null, new Department("departmentId", "departmentName")), new Status("statusId", "statusName"), new Role("roleId", "roleName")), new Device("id", "deviceId", "deviceName", DeviceStatus.ACTIVE, "deviceUrl", "deviceNote", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime()), new Room(0, "roomName", new Device("id", "deviceId", "deviceName", DeviceStatus.ACTIVE, "deviceUrl", "deviceNote", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime()))));
    }

    @Test
    void testUpdateExistedDailyLog() {
        DailyLog result = dailyLogService.updateExistedDailyLog(new DailyLog("dailyId", null, 0, null, null, 0d, 0d, 0d, true, true, 0d, 0d, true, 0d, 0d, null, null, DateType.NORMAL, "description", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), new Department("departmentId", "departmentName"))), null);
        Assertions.assertEquals(new DailyLog("dailyId", null, 0, null, null, 0d, 0d, 0d, true, true, 0d, 0d, true, 0d, 0d, null, null, DateType.NORMAL, "description", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), new Department("departmentId", "departmentName"))), result);
    }

    @Test
    void testGetLateCheckInDuration() {
        when(lateRequestFormRepositoryV2.findLateAndEarlyViolateByUserIdAndDate(anyString(), any(), any())).thenReturn(List.of(new LateFormResponse("userId", 0, null, "lateType")));

        dailyLogService.getLateCheckInDuration(new DailyLog("dailyId", null, 0, null, null, 0d, 0d, 0d, true, true, 0d, 0d, true, 0d, 0d, null, null, DateType.NORMAL, "description", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), new Department("departmentId", "departmentName"))), "userId", null, null);
    }

    @Test
    void testGetDateType() {
        when(holidayService.changeDailyLogToHoliday(any())).thenReturn(true);

        DateType result = dailyLogService.getDateType(null);
        Assertions.assertEquals(DateType.NORMAL, result);
    }

    @Test
    void testUpdateDailyLog() {
        when(dailyLogRepository.findByUserAndDate(any(), any())).thenReturn(null);
        when(checkoutAnalyzeSchedule.checkViolate(any(), any(), any())).thenReturn(true);

        boolean result = dailyLogService.updateDailyLog(new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), new Department("departmentId", "departmentName")), null, null, null);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testCheckViolate() {
        when(checkoutAnalyzeSchedule.checkViolate(any(), any(), any())).thenReturn(true);

        dailyLogService.checkViolate(new DailyLog("dailyId", null, 0, null, null, 0d, 0d, 0d, true, true, 0d, 0d, true, 0d, 0d, null, null, DateType.NORMAL, "description", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), new Department("departmentId", "departmentName"))), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), new Department("departmentId", "departmentName")), "reasons");
    }

    @Test
    void testInitEmployeeDayOff() {
        when(accountRepository.findAll()).thenReturn(List.of(new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), "createdBy", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), null, new Department("departmentId", "departmentName")), new Status("statusId", "statusName"), new Role("roleId", "roleName"))));

        List<DayOff> result = dailyLogService.initEmployeeDayOff();
        Assertions.assertEquals(List.of(new DayOff("dayOffId", 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0, new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), "createdBy", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 53).getTime(), null, new Department("departmentId", "departmentName")), new Status("statusId", "statusName"), new Role("roleId", "roleName")))), result);
    }

    @Test
    void testInitDayOff() {
        dailyLogService.initDayOff("accountId");
    }
}
//1
