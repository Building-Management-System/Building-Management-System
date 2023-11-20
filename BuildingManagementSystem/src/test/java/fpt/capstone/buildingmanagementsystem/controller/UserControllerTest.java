package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.request.AcceptChangeUserInfo;
import fpt.capstone.buildingmanagementsystem.model.request.GetUserInfoRequest;
import fpt.capstone.buildingmanagementsystem.model.response.GetAllUserInfoPending;
import fpt.capstone.buildingmanagementsystem.model.response.GetUserInfoResponse;
import fpt.capstone.buildingmanagementsystem.model.response.UserAccountResponse;
import fpt.capstone.buildingmanagementsystem.model.response.UserInfoResponse;
import fpt.capstone.buildingmanagementsystem.service.UserManageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserControllerTest {
    @Autowired
    UserManageService userManageService;
    @Autowired
    UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUserInfoPending() throws Exception {
        ResponseEntity<?> result = userController.getAllUserInfoPending();
        assertEquals(200, result.getStatusCodeValue());
        assertTrue(result.getBody() instanceof List);

        List<?> userList = (List<?>) result.getBody();
        assertEquals(1, userList.size());
    }

    @Test
    void testGetInfoUser() throws Exception {
        GetUserInfoRequest getUserInfoRequest = new GetUserInfoRequest();
        getUserInfoRequest.setUserId("f2dbbf96-1a65-4e72-805d-ee10ca9b50a6");

        ResponseEntity<?> result = userController.getInfoUser(getUserInfoRequest);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(new GetUserInfoResponse("John", "Doe",
                "Boyyyy", "03/04/2001",
                "0865965402", "LA",
                "LA", "sontung02hn@gmail.com", "unknown", "tech D1"), result.getBody());

    }

    @Test
    void testChangeUserInfo() throws Exception {
        when(userManageService.ChangeUserInfo(anyString(), any())).thenReturn(true);

        boolean result = userController.changeUserInfo("data", null);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testAcceptChangeUserInfo() throws Exception {
        when(userManageService.AcceptChangeUserInfo(any())).thenReturn(true);

        boolean result = userController.acceptChangeUserInfo(new AcceptChangeUserInfo("userId"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testRejectChangeUserInfo() throws Exception {
        when(userManageService.RejectChangeUserInfo(any())).thenReturn(true);

        boolean result = userController.rejectChangeUserInfo(new GetUserInfoRequest("userId"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testGetAllUserInfo() {
        ResponseEntity<?> result = userController.getAllUserInfo();
        assertEquals(200, result.getStatusCodeValue());
        assertTrue(result.getBody() instanceof List);

        List<?> userList = (List<?>) result.getBody();
        assertEquals(8, userList.size());
    }

    @Test
    void testGetManager() {
        when(userManageService.getManagerByDepartmentId(anyString())).thenReturn(List.of(new UserInfoResponse("accountId", "firstName", "lastName", "image", "roleName")));

        List<UserInfoResponse> result = userController.getManager("departmentId");
        Assertions.assertEquals(List.of(new UserInfoResponse("accountId", "firstName", "lastName", "image", "roleName")), result);
    }

    @Test
    void testGetUserAccount() {
        String userID = "11dea336-8be4-4399-bce6-c57d510b5275";

        List<UserAccountResponse> result = userController.getUserAccount(userID);
        assertEquals(7, result.size());
    }
}