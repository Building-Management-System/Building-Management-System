package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.entity.Department;
import fpt.capstone.buildingmanagementsystem.model.entity.Role;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.NotificationStatus;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.NotificationViewer;
import fpt.capstone.buildingmanagementsystem.model.request.NotificationDetailRequest;
import fpt.capstone.buildingmanagementsystem.model.request.PersonalPriorityRequest;
import fpt.capstone.buildingmanagementsystem.model.request.UnReadRequest;
import fpt.capstone.buildingmanagementsystem.model.response.*;
import fpt.capstone.buildingmanagementsystem.service.NotificationService;
import fpt.capstone.buildingmanagementsystem.service.NotificationServiceV2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class NotificationControllerTest {
    private MockMvc mockMvc;
    @Autowired
    NotificationService notificationService;
    @Autowired
    NotificationServiceV2 notificationServiceV2;
    @Autowired
    NotificationController notificationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testSaveNewNotification() throws Exception {
//        when(notificationService.saveNotification(anyString(), any(), any())).thenReturn(true);
//
//        boolean result = notificationController.saveNewNotification(
//                "data"
//                , new MultipartFile[]{null}
//                , new MultipartFile[]{null});
//        Assertions.assertEquals(true, result);
//    }

    public static MockMultipartFile createMockMultipartFile(String filePath) throws IOException {
        // Read content from the file
        byte[] fileContent = Files.readAllBytes(Paths.get(filePath));

        // Extract the file name from the path
        String fileName = Paths.get(filePath).getFileName().toString();

// Create a MockMultipartFile instance
        return new MockMultipartFile(
                "image",      // The name of the parameter for the file in your controller method
                fileName,    // The original file name
                Files.probeContentType(Paths.get(filePath)), // The content type (you can change this based on your needs)
                fileContent  // The file content as bytes
        );
    }

    @Test
    public void testSaveNewNotification() throws Exception {

//        MockMultipartFile imageFile = new MockMultipartFile("image", "ass.png", "image/png", Files.readAllBytes(Paths.get("D:\\Wallpaper\\ass.png")));
//        MockMultipartFile docxFile = new MockMultipartFile("file", "VNR202.docx", "application/octet-stream", Files.readAllBytes(Paths.get("D:\\Wallpaper\\VNR202.docx")));
        MockMultipartFile mockMultipartFile = createMockMultipartFile("D:\\Wallpaper\\ass.png");


        String jsonData = "{ \"buttonStatus\": \"upload\"," +
                " \"userId\": \"0dff5d5c-095d-4386-91f2-82bdb7eba342\"," +
                " \"title\": \"Noti 4\"," +
                " \"sendAllStatus\": false," +
                " \"receiverId\":[\"55fd796e-6e33-4b17-b6d9-d32aef9fce3f\",\"f2dbbf96-1a65-4e72-805d-ee10ca9b50a6\"]," +
                " \"priority\": true," +
                " \"content\": \"Noti 4\"}";
//                " \"uploadDatePlan\": \"2023-11-16 17:08:00\"}";

        boolean result = notificationController.saveNewNotification(jsonData, new MockMultipartFile[]{mockMultipartFile}, new MockMultipartFile[]{});

        assertTrue(result);
    }

    @Test
    void testEditNotification() throws Exception {
        when(notificationService.changeNotification(anyString(), any(), any())).thenReturn(true);

        boolean result = notificationController.editNotification("data", new MultipartFile[]{null}, new MultipartFile[]{null});
        Assertions.assertEquals(true, result);
    }

    @Test
    void testGetAllNotificationByUser() {
        when(notificationServiceV2.getAllNotificationByUser(anyString())).thenReturn(new NotificationTitleResponse(0, List.of(new NotificationResponse("notificationId", "title", new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), true, "userId", new Department("departmentId", "departmentName")))));

        NotificationTitleResponse result = notificationController.getAllNotificationByUser("userId");
        Assertions.assertEquals(new NotificationTitleResponse(0, List.of(new NotificationResponse("notificationId", "title", new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), true, "userId", new Department("departmentId", "departmentName")))), result);
    }

    @Test
    void testGetListNotificationByUserId() {
        when(notificationServiceV2.getListNotificationByCreator(anyString())).thenReturn(List.of(new NotificationDetailResponse("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), NotificationStatus.DRAFT, true, "creatorId", new Department("departmentId", "departmentName"), true, true, NotificationViewer.SENDER, true, true)));

        List<NotificationDetailResponse> result = notificationController.getListNotificationByUserId("userId");
        Assertions.assertEquals(List.of(new NotificationDetailResponse("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), NotificationStatus.DRAFT, true, "creatorId", new Department("departmentId", "departmentName"), true, true, NotificationViewer.SENDER, true, true)), result);
    }

    @Test
    void testGetListUploadedNotificationByCreator() {
        when(notificationServiceV2.getListUploadedNotificationByCreator(anyString())).thenReturn(List.of(new NotificationDetailResponse("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), NotificationStatus.DRAFT, true, "creatorId", new Department("departmentId", "departmentName"), true, true, NotificationViewer.SENDER, true, true)));

        List<NotificationDetailResponse> result = notificationController.getListUploadedNotificationByCreator("userId");
        Assertions.assertEquals(List.of(new NotificationDetailResponse("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), NotificationStatus.DRAFT, true, "creatorId", new Department("departmentId", "departmentName"), true, true, NotificationViewer.SENDER, true, true)), result);
    }

    @Test
    void testGetListUploadedNotificationByUserId() {
        when(notificationServiceV2.getListUploadedNotificationByUserId(anyString())).thenReturn(List.of(new NotificationDetailResponse("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), NotificationStatus.DRAFT, true, "creatorId", new Department("departmentId", "departmentName"), true, true, NotificationViewer.SENDER, true, true)));

        List<NotificationDetailResponse> result = notificationController.getListUploadedNotificationByUserId("userId");
        Assertions.assertEquals(List.of(new NotificationDetailResponse("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), NotificationStatus.DRAFT, true, "creatorId", new Department("departmentId", "departmentName"), true, true, NotificationViewer.SENDER, true, true)), result);
    }

    @Test
    void testGetListDraftNotificationByUserId() {
        when(notificationServiceV2.getListDraftNotificationByCreator(anyString())).thenReturn(List.of(new NotificationDetailResponse("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), NotificationStatus.DRAFT, true, "creatorId", new Department("departmentId", "departmentName"), true, true, NotificationViewer.SENDER, true, true)));

        List<NotificationDetailResponse> result = notificationController.getListDraftNotificationByUserId("userId");
        Assertions.assertEquals(List.of(new NotificationDetailResponse("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), NotificationStatus.DRAFT, true, "creatorId", new Department("departmentId", "departmentName"), true, true, NotificationViewer.SENDER, true, true)), result);
    }

    @Test
    void testGetListScheduledNotificationByUserId() {
        when(notificationServiceV2.getListScheduledNotificationByCreator(anyString())).thenReturn(List.of(new NotificationDetailResponse("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), NotificationStatus.DRAFT, true, "creatorId", new Department("departmentId", "departmentName"), true, true, NotificationViewer.SENDER, true, true)));

        List<NotificationDetailResponse> result = notificationController.getListScheduledNotificationByUserId("userId");
        Assertions.assertEquals(List.of(new NotificationDetailResponse("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), NotificationStatus.DRAFT, true, "creatorId", new Department("departmentId", "departmentName"), true, true, NotificationViewer.SENDER, true, true)), result);
    }

    @Test
    void testMarkToReadByNotification() {
        when(notificationService.markAsRead(anyString(), anyString())).thenReturn(true);

        boolean result = notificationController.markToReadByNotification(new UnReadRequest("notificationId", "userId"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testMarkToUnReadByNotification() {
        when(notificationService.markAsUnRead(anyString(), anyString())).thenReturn(true);

        boolean result = notificationController.markToUnReadByNotification(new UnReadRequest("notificationId", "userId"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testSetPersonalPriority() {
        when(notificationService.setPersonalPriority(anyString(), anyString())).thenReturn(true);

        boolean result = notificationController.setPersonalPriority(new PersonalPriorityRequest("notificationId", "userId"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testUnsetPersonalPriority() {
        when(notificationService.unsetPersonalPriority(anyString(), anyString())).thenReturn(true);

        boolean result = notificationController.unsetPersonalPriority(new PersonalPriorityRequest("notificationId", "userId"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testGetNotificationByUserIdAndNotificationId() {
        when(notificationServiceV2.getNotificationDetailByUserIdAndNotificationId(anyString(), anyString())).thenReturn(new NotificationDetailResponseForDetail("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), NotificationStatus.DRAFT, true, "creatorId", "firstName", "lastName", new Role("roleId", "roleName"), "userName", new Department("departmentId", "departmentName"), "creatorImage", true, List.of(new NotificationFileResponse("fileId", new byte[]{(byte) 0}, "fileName", "type")), List.of(new NotificationImageResponse("imageId", "imageFileName"))));

        NotificationDetailResponseForDetail result = notificationController.getNotificationByUserIdAndNotificationId(new NotificationDetailRequest("userId", "notificationId"));
        Assertions.assertEquals(new NotificationDetailResponseForDetail("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), NotificationStatus.DRAFT, true, "creatorId", "firstName", "lastName", new Role("roleId", "roleName"), "userName", new Department("departmentId", "departmentName"), "creatorImage", true, List.of(new NotificationFileResponse("fileId", new byte[]{(byte) 0}, "fileName", "type")), List.of(new NotificationImageResponse("imageId", "imageFileName"))), result);
    }

    @Test
    void testGetNotificationByCreatorAndNotificationId() {
        when(notificationServiceV2.getNotificationDetailByCreator(anyString(), anyString())).thenReturn(new NotificationDetailResponseForCreator("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), NotificationStatus.DRAFT, true, "creatorId", "userName", "firstName", "lastName", new Role("roleId", "roleName"), "creatorImage", true, List.of(new UserAccountResponse("accountId", "username", "departmentId", "departmentName")), List.of(new NotificationFileResponse("fileId", new byte[]{(byte) 0}, "fileName", "type")), List.of(new NotificationImageResponse("imageId", "imageFileName"))));

        NotificationDetailResponseForCreator result = notificationController.getNotificationByCreatorAndNotificationId(new NotificationDetailRequest("userId", "notificationId"));
        Assertions.assertEquals(new NotificationDetailResponseForCreator("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), NotificationStatus.DRAFT, true, "creatorId", "userName", "firstName", "lastName", new Role("roleId", "roleName"), "creatorImage", true, List.of(new UserAccountResponse("accountId", "username", "departmentId", "departmentName")), List.of(new NotificationFileResponse("fileId", new byte[]{(byte) 0}, "fileName", "type")), List.of(new NotificationImageResponse("imageId", "imageFileName"))), result);
    }

    @Test
    void testGetListScheduledNotificationByDepartmentOfCreator() {
        when(notificationServiceV2.getListScheduledNotificationByDepartmentOfCreator(anyString())).thenReturn(List.of(new NotificationDetailResponse("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), NotificationStatus.DRAFT, true, "creatorId", new Department("departmentId", "departmentName"), true, true, NotificationViewer.SENDER, true, true)));

        List<NotificationDetailResponse> result = notificationController.getListScheduledNotificationByDepartmentOfCreator("userId");
        Assertions.assertEquals(List.of(new NotificationDetailResponse("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 16, 15, 58).getTime(), NotificationStatus.DRAFT, true, "creatorId", new Department("departmentId", "departmentName"), true, true, NotificationViewer.SENDER, true, true)), result);
    }

    @Test
    void testSetNotificationHidden() {
        when(notificationService.notificationHidden(anyString(), anyString())).thenReturn(true);

        boolean result = notificationController.setNotificationHidden(new PersonalPriorityRequest("notificationId", "userId"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testDeleteNotification() {
        when(notificationService.deleteNotification(anyString(), anyString())).thenReturn(true);

        boolean result = notificationController.deleteNotification(new PersonalPriorityRequest("notificationId", "userId"));
        Assertions.assertEquals(true, result);
    }
}


