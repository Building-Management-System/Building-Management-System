package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.model.entity.ChangeLog;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.ChangeLogType;
import fpt.capstone.buildingmanagementsystem.model.request.ChangeLogRequest;
import fpt.capstone.buildingmanagementsystem.model.response.ChangeLogResponse;
import fpt.capstone.buildingmanagementsystem.repository.ChangeLogRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.mockito.Mockito.*;

class ChangeLogServiceTest {
    @Mock
    ChangeLogRepository changeLogRepository;
    @InjectMocks
    ChangeLogService changeLogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllChangeLogByEmployeeIdAndMonth() {
        when(changeLogRepository.getChangeLogByUserIdAndMonth(anyString(), anyInt())).thenReturn(List.of(new ChangeLog("changeLogId", null, null, null, 0d, true, "reason", ChangeLogType.FROM_REQUEST, new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 50).getTime(), null, null)));

        List<ChangeLogResponse> result = changeLogService.getAllChangeLogByEmployeeIdAndMonth(new ChangeLogRequest("employeeId", 0, null));
        Assertions.assertEquals(List.of(new ChangeLogResponse("changeLogId", null, null, 0d, true, "reason", ChangeLogType.FROM_REQUEST, null, new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 50).getTime(), "managerId", "employeeId", "userName", "firstName", "lastName")), result);
    }

    @Test
    void testGetAllLogsInDay() {
        when(changeLogRepository.getChangeLogByUserIdAndDate(anyString(), any())).thenReturn(List.of(new ChangeLog("changeLogId", null, null, null, 0d, true, "reason", ChangeLogType.FROM_REQUEST, new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 50).getTime(), null, null)));

        List<ChangeLogResponse> result = changeLogService.getAllLogsInDay(new ChangeLogRequest("employeeId", 0, null));
        Assertions.assertEquals(List.of(new ChangeLogResponse("changeLogId", null, null, 0d, true, "reason", ChangeLogType.FROM_REQUEST, null, new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 50).getTime(), "managerId", "employeeId", "userName", "firstName", "lastName")), result);
    }
}