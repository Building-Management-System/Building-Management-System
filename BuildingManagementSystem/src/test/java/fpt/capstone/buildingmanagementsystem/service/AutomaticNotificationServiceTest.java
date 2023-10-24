package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.model.entity.*;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.TopicEnum;
import fpt.capstone.buildingmanagementsystem.model.request.AcceptOrRejectChangeUserInfo;
import fpt.capstone.buildingmanagementsystem.model.request.ApprovalNotificationEvaluate;
import fpt.capstone.buildingmanagementsystem.model.request.ApprovalNotificationRequest;
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

import static org.mockito.Mockito.*;

class AutomaticNotificationServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    NotificationRepository notificationRepository;
    @Mock
    UnreadMarkRepository unreadMarkRepository;
    @Mock
    NotificationReceiverRepository notificationReceiverRepository;
    @Mock
    DepartmentRepository departmentRepository;
    @InjectMocks
    AutomaticNotificationService automaticNotificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendApprovalRequestNotification() {
        when(departmentRepository.findByUserId(anyString())).thenReturn(null);

        NotificationAcceptResponse result = automaticNotificationService.sendApprovalRequestNotification(new ApprovalNotificationRequest("ticketId", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), new Department("departmentId", "departmentName")), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), new Department("departmentId", "departmentName")), TopicEnum.ATTENDANCE_REQUEST, true, "reason"));
        Assertions.assertEquals(new NotificationAcceptResponse("notificationId", "userId", "receiverId", "senderName", true, "title", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), new Department("departmentId", "departmentName")), result);
    }

    @Test
    void testSendApprovalTicketNotification() {
        automaticNotificationService.sendApprovalTicketNotification(new ApprovalNotificationRequest("ticketId", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), new Department("departmentId", "departmentName")), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), new Department("departmentId", "departmentName")), TopicEnum.ATTENDANCE_REQUEST, true, "reason"));
    }

    @Test
    void testSendChangeNotification() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);

        automaticNotificationService.sendChangeNotification(new AcceptOrRejectChangeUserInfo("hrId", "userId"), "status");
    }

    @Test
    void testSendApprovalEvaluateRequest() {
        when(departmentRepository.findByUserId(anyString())).thenReturn(null);

        NotificationAcceptResponse result = automaticNotificationService.sendApprovalEvaluateRequest(new ApprovalNotificationEvaluate(new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), new Department("departmentId", "departmentName")), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), new Department("departmentId", "departmentName")), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), new Department("departmentId", "departmentName")), true, "hrNote"));
        Assertions.assertEquals(new NotificationAcceptResponse("notificationId", "userId", "receiverId", "senderName", true, "title", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 23, 44).getTime(), new Department("departmentId", "departmentName")), result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme