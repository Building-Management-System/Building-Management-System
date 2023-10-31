package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.model.entity.DailyLog;
import fpt.capstone.buildingmanagementsystem.model.entity.EmailCode;
import fpt.capstone.buildingmanagementsystem.model.entity.Holiday;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.DateType;
import fpt.capstone.buildingmanagementsystem.model.request.HolidayDeleteRequest;
import fpt.capstone.buildingmanagementsystem.model.request.HolidaySaveRequest;
import fpt.capstone.buildingmanagementsystem.model.response.HolidayResponse;
import fpt.capstone.buildingmanagementsystem.repository.*;
import fpt.capstone.buildingmanagementsystem.security.PasswordEncode;
import fpt.capstone.buildingmanagementsystem.service.schedule.CheckoutAnalyzeSchedule;
import fpt.capstone.buildingmanagementsystem.until.EmailSender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.mockito.Mockito.*;

class HolidayServiceTest {
    @Mock
    AttendanceService attendanceService;
    @Mock
    HolidayRepository holidayRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    DailyLogRepository dailyLogRepository;
    @Mock
    CheckoutAnalyzeSchedule checkoutAnalyzeSchedule;
    @Mock
    AccountRepository accountRepository;
    @Mock
    PasswordEncode passwordEncode;
    @Mock
    EmailSender emailSender;
    @Mock
    EmailCodeRepository emailCodeRepository;
    @InjectMocks
    HolidayService holidayService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveHoliday() throws ParseException {
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        when(dailyLogRepository.getDailyLogsByFromDateAndToDate(any(), any())).thenReturn(List.of(new DailyLog("dailyId", null, 0, null, null, 0d, 0d, 0d, true, true, 0d, 0d, true, 0d, 0d, null, null, DateType.NORMAL, "description", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 9).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 9).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 9).getTime(), null, null))));

        boolean result = holidayService.saveHoliday(new HolidaySaveRequest("title", "content", "toDate", "fromDate", "userId"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testDeleteHoliday() {
        when(attendanceService.getCheckWeekend(any())).thenReturn(0);
        when(dailyLogRepository.getDailyLogsByFromDateAndToDate(any(), any())).thenReturn(List.of(new DailyLog("dailyId", null, 0, null, null, 0d, 0d, 0d, true, true, 0d, 0d, true, 0d, 0d, null, null, DateType.NORMAL, "description", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 9).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 9).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 9).getTime(), null, null))));
        when(checkoutAnalyzeSchedule.checkViolate(any(), any(), any())).thenReturn(true);

        boolean result = holidayService.deleteHoliday(new HolidayDeleteRequest("holidayId"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testListAllHoliday() {
        when(holidayRepository.findAll()).thenReturn(List.of(new Holiday("holidayId", "title", "content", null, null, new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 9).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 9).getTime(), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 9).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 9).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 9).getTime(), null, null))));

        List<HolidayResponse> result = holidayService.listAllHoliday();
        Assertions.assertEquals(List.of(new HolidayResponse("holidayId", "title", "content", null, null, "username")), result);
    }

    @Test
    void testChangeDailyLogToHoliday() {
        when(holidayRepository.findAll()).thenReturn(List.of(new Holiday("holidayId", "title", "content", null, null, new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 9).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 9).getTime(), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 9).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 9).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 9).getTime(), null, null))));

        boolean result = holidayService.changeDailyLogToHoliday(null);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testSendHolidayEmail() {
        when(accountRepository.findByUsername(anyString())).thenReturn(null);

        boolean result = holidayService.sendHolidayEmail("userName");
        Assertions.assertEquals(true, result);
    }

    @Test
    void testCheckHolidayCode() {
        when(emailCodeRepository.findByCodeAndUserId(anyString(), anyString())).thenReturn(List.of(new EmailCode("emailCodeId", "code", "userId")));

        boolean result = holidayService.checkHolidayCode("code", "userId");
        Assertions.assertEquals(true, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme