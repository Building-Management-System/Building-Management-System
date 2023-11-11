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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.*;

class UserControllerTest {
    @Mock
    UserManageService userManageService;
    @InjectMocks
    UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUserInfoPending() throws Exception {
        when(userManageService.getAllUserNotVerify()).thenReturn(List.of(new GetAllUserInfoPending("accountId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "country", "city", "email", "image")));

        ResponseEntity<?> result = userController.getAllUserInfoPending();
        Assertions.assertEquals(null, result);
    }

    @Test
    void testGetInfoUser() throws Exception {
        when(userManageService.getInfoUser(any())).thenReturn(new GetUserInfoResponse("firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "country", "city", "email", "image", "departmentName"));

        ResponseEntity<?> result = userController.getInfoUser(new GetUserInfoRequest("userId"));
        Assertions.assertEquals(null, result);
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
        when(userManageService.getAllUserInfo()).thenReturn(List.of(new UserInfoResponse("accountId", "firstName", "lastName", "image", "roleName")));

        ResponseEntity<?> result = userController.getAllUserInfo();
        Assertions.assertEquals(null, result);
    }

    @Test
    void testGetManager() {
        when(userManageService.getManagerByDepartmentId(anyString())).thenReturn(List.of(new UserInfoResponse("accountId", "firstName", "lastName", "image", "roleName")));

        List<UserInfoResponse> result = userController.getManager("departmentId");
        Assertions.assertEquals(List.of(new UserInfoResponse("accountId", "firstName", "lastName", "image", "roleName")), result);
    }

    @Test
    void testGetUserAccount() {
        when(userManageService.getUserAccount(anyString())).thenReturn(List.of(new UserAccountResponse("accountId", "username", "departmentId", "departmentName")));

        List<UserAccountResponse> result = userController.getUserAccount("userId");
        Assertions.assertEquals(List.of(new UserAccountResponse("accountId", "username", "departmentId", "departmentName")), result);
    }
}