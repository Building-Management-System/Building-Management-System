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
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class NotificationControllerTest {
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

    @Test
    public void testSaveNewNotification() throws Exception {
        MockMultipartFile imageFile = createMockMultipartFile("image", "R1.jpg");

        MockMultipartFile docxFile = createMockMultipartFile("file", "gpt.txt");

        String jsonData = "{ \"buttonStatus\": \"upload\"," +
                " \"userId\": \"3f103761-d5bf-4686-a435-9c32d648ceca\"," +
                " \"title\": \"Noti  upload 4\"," +
                " \"sendAllStatus\": false," +
                " \"receiverId\":[\"55fd796e-6e33-4b17-b6d9-d32aef9fce3f\"]," +
                " \"priority\": true," +
                " \"content\": \"Noti upload 4\"," +
                " \"uploadDatePlan\": \"2023-11-24 19:40:00\"}";

        boolean result = notificationController.saveNewNotification(jsonData, new MockMultipartFile[]{imageFile}, new MockMultipartFile[]{docxFile});
        assertEquals(true, result);
    }

    private MockMultipartFile createMockMultipartFile(String paramName, String fileName) throws Exception {
        Path filePath = Paths.get("D:\\Wallpaper\\" + fileName);
        String originalFileName = fileName;
        String contentType = Files.probeContentType(filePath);
        byte[] content = Files.readAllBytes(filePath);
        return new MockMultipartFile(paramName, originalFileName, contentType, content);
    }

    @Test
    void testEditNotification() throws Exception {
        String user_id = "";

        boolean result = notificationController.editNotification("data", new MultipartFile[]{null}, new MultipartFile[]{null});
        Assertions.assertEquals(true, result);
    }


    //
    @Test
    void testGetAllNotificationByUser() {
        String user_id2="???Adfsdfgs";
        String user_id="55fd796e-6e33-4b17-b6d9-d32aef9fce3f";

        NotificationTitleResponse result = notificationController.getAllNotificationByUser(user_id);

        assertEquals(30, result.getTotal());
    }

    @Test
    void testGetListNotificationByUserId() {
        String creator_userId = "11dea336-8be4-4399-bce6-c57d510b5275";

        List<NotificationDetailResponse> result = notificationController.getListNotificationByUserId(creator_userId);
        assertNotNull(result);
        assertEquals(8, result.size());
    }

    @Test
    void testGetListUploadedNotificationByCreator() {
        String creator_userId = "11dea336-8be4-4399-bce6-c57d510b5275";

        List<NotificationDetailResponse> result = notificationController.getListUploadedNotificationByCreator(creator_userId);

        assertNotNull(result);
        assertEquals(5, result.size());
    }

    @Test
    void testGetListUploadedNotificationByUserId() {
        String userId = "94b38a94-bb4a-4adb-acc9-34bde9babd4e";

        List<NotificationDetailResponse> result = notificationController.getListUploadedNotificationByUserId(userId);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testGetListDraftNotificationByUserId() {
        String userId = "11dea336-8be4-4399-bce6-c57d510b5275";

        List<NotificationDetailResponse> result = notificationController.getListDraftNotificationByUserId(userId);
        assertNotNull(result);
        assertEquals(4, result.size());
    }

    @Test
    void testGetListScheduledNotificationByUserId() {
        String userId = "3a5cccac-9490-4b9b-9e1e-16ce220b35cb";

        List<NotificationDetailResponse> result = notificationController.getListScheduledNotificationByUserId(userId);
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    /////bat loi nua
    @Test
    void testMarkToReadByNotification() {
        UnReadRequest unReadRequest = new UnReadRequest();
        unReadRequest.setNotificationId("d011710d-80e6-463c-ac9f-b3b419bae32c");
        unReadRequest.setUserId("0dff5d5c-095d-4386-91f2-82bdb7eba342");

        boolean result = notificationController.markToReadByNotification(unReadRequest);

        assertEquals(true, result);
    }

    @Test
    void testMarkToUnReadByNotification() {
        UnReadRequest unReadRequest = new UnReadRequest();
        unReadRequest.setNotificationId("d011710d-80e6-463c-ac9f-b3b419bae32c");
        unReadRequest.setUserId("0dff5d5c-095d-4386-91f2-82bdb7eba342");

        boolean result = notificationController.markToUnReadByNotification(unReadRequest);

        assertEquals(true, result);
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
        when(notificationServiceV2.getNotificationDetailByUserIdAndNotificationId(anyString(), anyString())).thenReturn(new NotificationDetailResponseForDetail("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.NOVEMBER, 22, 13, 52).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 22, 13, 52).getTime(), NotificationStatus.DRAFT, true, "creatorId", "firstName", "lastName", new Role("roleId", "roleName"), "userName", new Department("departmentId", "departmentName"), "creatorImage", true, List.of(new NotificationFileResponse("fileId", "fileName", "type")), List.of(new NotificationImageResponse("imageId", "imageFileName"))));

        NotificationDetailResponseForDetail result = notificationController.getNotificationByUserIdAndNotificationId(new NotificationDetailRequest("userId", "notificationId"));
        Assertions.assertEquals(new NotificationDetailResponseForDetail("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.NOVEMBER, 22, 13, 52).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 22, 13, 52).getTime(), NotificationStatus.DRAFT, true, "creatorId", "firstName", "lastName", new Role("roleId", "roleName"), "userName", new Department("departmentId", "departmentName"), "creatorImage", true, List.of(new NotificationFileResponse("fileId", "fileName", "type")), List.of(new NotificationImageResponse("imageId", "imageFileName"))), result);
    }

    @Test
    void testGetNotificationByCreatorAndNotificationId() {
        when(notificationServiceV2.getNotificationDetailByCreator(anyString(), anyString())).thenReturn(new NotificationDetailResponseForCreator("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.NOVEMBER, 22, 13, 52).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 22, 13, 52).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 22, 13, 52).getTime(), NotificationStatus.DRAFT, true, "creatorId", "userName", "firstName", "lastName", new Role("roleId", "roleName"), "creatorImage", true, List.of(new UserAccountResponse("accountId", "username", "departmentId", "departmentName")), List.of(new NotificationFileResponse("fileId", "fileName", "type")), List.of(new NotificationImageResponse("imageId", "imageFileName"))));

        NotificationDetailResponseForCreator result = notificationController.getNotificationByCreatorAndNotificationId(new NotificationDetailRequest("userId", "notificationId"));
        Assertions.assertEquals(new NotificationDetailResponseForCreator("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.NOVEMBER, 22, 13, 52).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 22, 13, 52).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 22, 13, 52).getTime(), NotificationStatus.DRAFT, true, "creatorId", "userName", "firstName", "lastName", new Role("roleId", "roleName"), "creatorImage", true, List.of(new UserAccountResponse("accountId", "username", "departmentId", "departmentName")), List.of(new NotificationFileResponse("fileId", "fileName", "type")), List.of(new NotificationImageResponse("imageId", "imageFileName"))), result);
    }

    @Test
    void testGetListScheduledNotificationByDepartmentOfCreator() {
        when(notificationServiceV2.getListScheduledNotificationByDepartmentOfCreator(anyString())).thenReturn(List.of(new NotificationDetailResponse("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.NOVEMBER, 22, 13, 52).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 22, 13, 52).getTime(), NotificationStatus.DRAFT, true, "creatorId", new Department("departmentId", "departmentName"), true, true, NotificationViewer.SENDER, true, true)));

        List<NotificationDetailResponse> result = notificationController.getListScheduledNotificationByDepartmentOfCreator("userId");
        Assertions.assertEquals(List.of(new NotificationDetailResponse("notificationId", "title", "content", new GregorianCalendar(2023, Calendar.NOVEMBER, 22, 13, 52).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 22, 13, 52).getTime(), NotificationStatus.DRAFT, true, "creatorId", new Department("departmentId", "departmentName"), true, true, NotificationViewer.SENDER, true, true)), result);
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
