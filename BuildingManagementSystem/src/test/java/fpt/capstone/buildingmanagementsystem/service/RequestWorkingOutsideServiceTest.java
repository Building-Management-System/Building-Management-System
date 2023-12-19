package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.model.entity.Department;
import fpt.capstone.buildingmanagementsystem.model.request.WorkingOutsideRequest;
import fpt.capstone.buildingmanagementsystem.model.response.NotificationAcceptResponse;
import fpt.capstone.buildingmanagementsystem.repository.*;
import fpt.capstone.buildingmanagementsystem.service.schedule.CheckoutAnalyzeSchedule;
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

class RequestWorkingOutsideServiceTest {
    @Mock
    DailyLogRepository dailyLogRepository;
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
    WorkingOutsideFormRepository workingOutsideRepository;
    @Mock
    CheckoutAnalyzeSchedule checkoutAnalyzeSchedule;
    @InjectMocks
    RequestWorkingOutsideService requestWorkingOutsideService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAcceptWorkingOutsideRequest() {
        when(dailyLogRepository.findByUserAndDate(any(), any())).thenReturn(null);
        when(requestTicketRepository.findByTicketRequest(any())).thenReturn(List.of(null));
        when(automaticNotificationService.sendApprovalRequestNotification(any())).thenReturn(new NotificationAcceptResponse("notificationId", "userId", "receiverId", "senderName", true, "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new Department("departmentId", "departmentName")));
        when(checkoutAnalyzeSchedule.checkWorkingOutside(any(), any(), any())).thenReturn(0d);
        when(checkoutAnalyzeSchedule.checkViolate(any(), any(), any())).thenReturn(true);

        boolean result = requestWorkingOutsideService.acceptWorkingOutsideRequest("acceptWorkingOutsideRequestId");
        Assertions.assertEquals(true, result);
    }

    @Test
    void testRejectWorkingOutside() {
        when(requestTicketRepository.findByTicketRequest(any())).thenReturn(List.of(null));
        when(automaticNotificationService.sendApprovalRequestNotification(any())).thenReturn(new NotificationAcceptResponse("notificationId", "userId", "receiverId", "senderName", true, "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new Department("departmentId", "departmentName")));

        boolean result = requestWorkingOutsideService.rejectWorkingOutside(new WorkingOutsideRequest("workOutsideRequestId", "content"));
        Assertions.assertEquals(true, result);
    }
}
