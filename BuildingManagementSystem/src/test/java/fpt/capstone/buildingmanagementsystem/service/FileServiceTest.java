package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.model.entity.*;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.NotificationStatus;
import fpt.capstone.buildingmanagementsystem.repository.FileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.mockito.Mockito.*;

class FileServiceTest {
    @Mock
    FileRepository fileRepository;
    @InjectMocks
    FileService fileService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStore() throws IOException {
        fileService.store(new MultipartFile[]{null}, new Notification("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 5).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 5).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 5).getTime(), NotificationStatus.DRAFT, true, new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 5).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 5).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 5).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 5).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 5).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), new Department("departmentId", "departmentName"))));
    }

    @Test
    void testGetALlFiles() {
        ResponseEntity<?> result = fileService.getALlFiles();
        Assertions.assertEquals(null, result);
    }

    @Test
    void testGetFile() {
        ResponseEntity<?> result = fileService.getFile("id");
        Assertions.assertEquals(null, result);
    }
    //5
}
