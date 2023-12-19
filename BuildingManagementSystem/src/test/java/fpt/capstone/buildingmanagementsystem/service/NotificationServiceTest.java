package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.mapper.NotificationMapper;
import fpt.capstone.buildingmanagementsystem.model.entity.*;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.NotificationStatus;
import fpt.capstone.buildingmanagementsystem.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.mockito.Mockito.*;

class NotificationServiceTest {
    @Mock
    NotificationHiddenRepository notificationHiddenRepository;
    @Mock
    NotificationReceiverRepository notificationReceiverRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    NotificationMapper notificationMapper;
    @Mock
    NotificationRepository notificationRepository;
    @Mock
    UnreadMarkRepository unreadMarkRepository;
    @Mock
    NotificationImageRepository notificationImageRepository;
    @Mock
    NotificationFileRepository notificationFileRepository;
    @Mock
    FileService fileService;
    @Mock
    PersonalPriorityRepository personalPriorityRepository;
    @InjectMocks
    NotificationService notificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testMarkAsRead() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        when(unreadMarkRepository.findByNotificationAndUser(any(), any())).thenReturn(null);

        boolean result = notificationService.markAsRead("notificationId", "userId");
        Assertions.assertEquals(true, result);
    }

    @Test
    void testMarkAsUnRead() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        when(unreadMarkRepository.findByNotificationAndUser(any(), any())).thenReturn(null);

        boolean result = notificationService.markAsUnRead("notificationId", "userId");
        Assertions.assertEquals(true, result);
    }

    @Test
    void testUnsetPersonalPriority() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        when(personalPriorityRepository.findByNotificationAndUser(any(), any())).thenReturn(List.of(new PersonalPriority("personaPriorityId", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), new Department("departmentId", "departmentName")), new Notification("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), NotificationStatus.DRAFT, true, new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), new Department("departmentId", "departmentName"))))));

        boolean result = notificationService.unsetPersonalPriority("notificationId", "userId");
        Assertions.assertEquals(true, result);
    }

    @Test
    void testSetPersonalPriority() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        when(personalPriorityRepository.findByNotificationAndUser(any(), any())).thenReturn(List.of(new PersonalPriority("personaPriorityId", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), new Department("departmentId", "departmentName")), new Notification("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), NotificationStatus.DRAFT, true, new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), new Department("departmentId", "departmentName"))))));

        boolean result = notificationService.setPersonalPriority("notificationId", "userId");
        Assertions.assertEquals(true, result);
    }

    @Test
    void testDeleteNotification() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);

        boolean result = notificationService.deleteNotification("notificationId", "userId");
        Assertions.assertEquals(true, result);
    }

    @Test
    void testNotificationHidden() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);

        boolean result = notificationService.notificationHidden("notificationId", "userId");
        Assertions.assertEquals(true, result);
    }

    @Test
    void testHiddenNotificationSendAll() {
        when(notificationRepository.getSendToAllNotification()).thenReturn(List.of(new Notification("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), NotificationStatus.DRAFT, true, new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), new Department("departmentId", "departmentName")))));

        notificationService.hiddenNotificationSendAll(new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), new Department("departmentId", "departmentName")));
    }

    @Test
    void testDeleteFromHiddenNotification() {
        notificationService.deleteFromHiddenNotification(new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 46).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), new Department("departmentId", "departmentName")));
    }
}
