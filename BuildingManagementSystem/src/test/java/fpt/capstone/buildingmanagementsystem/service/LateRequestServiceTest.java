package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.model.entity.Department;
import fpt.capstone.buildingmanagementsystem.model.request.LateMessageRequest;
import fpt.capstone.buildingmanagementsystem.model.response.NotificationAcceptResponse;
import fpt.capstone.buildingmanagementsystem.repository.*;
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

class LateRequestServiceTest {
    @Mock
    TicketRepositoryv2 ticketRepositoryv2;
    @Mock
    RequestTicketRepository requestTicketRepository;
    @Mock
    RequestMessageRepository requestMessageRepository;
    @Mock
    DepartmentRepository departmentRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    TicketRepository ticketRepository;
    @Mock
    RequestOtherService requestOtherService;
    @Mock
    AutomaticNotificationService automaticNotificationService;
    @Mock
    LateRequestFormRepository lateRequestFormRepository;
    @Mock
    AttendanceService attendanceService;
    @Mock
    DailyLogRepository dailyLogRepository;
    @Mock
    LateRequestFormRepositoryV2 lateRequestFormRepositoryV2;
    @Mock
    DailyLogService dailyLogService;
    @InjectMocks
    LateRequestService lateRequestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAcceptLateRequest() {
        when(requestTicketRepository.findByTicketRequest(any())).thenReturn(List.of(null));
        when(automaticNotificationService.sendApprovalRequestNotification(any())).thenReturn(new NotificationAcceptResponse("notificationId", "userId", "receiverId", "senderName", true, "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 23).getTime(), new Department("departmentId", "departmentName")));
        when(dailyLogRepository.findByUserAndDate(any(), any())).thenReturn(null);

        boolean result = lateRequestService.acceptLateRequest("acceptLateRequestId");
        Assertions.assertEquals(true, result);
    }

    @Test
    void testRejectLateRequest() {
        when(requestTicketRepository.findByTicketRequest(any())).thenReturn(List.of(null));
        when(automaticNotificationService.sendApprovalRequestNotification(any())).thenReturn(new NotificationAcceptResponse("notificationId", "userId", "receiverId", "senderName", true, "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 23).getTime(), new Department("departmentId", "departmentName")));

        boolean result = lateRequestService.rejectLateRequest(new LateMessageRequest("lateMessageRequestId", "content"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testUpdateLateRequest() {
        when(dailyLogRepository.findByUserAndDate(any(), any())).thenReturn(null);

        lateRequestService.updateLateRequest(null, null, "reasons");
    }
}

