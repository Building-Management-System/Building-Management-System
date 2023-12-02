package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.mapper.UserMapper;
import fpt.capstone.buildingmanagementsystem.mapper.UserPendingMapper;
import fpt.capstone.buildingmanagementsystem.model.request.GetUserInfoRequest;
import fpt.capstone.buildingmanagementsystem.model.response.GetAllUserInfoPending;
import fpt.capstone.buildingmanagementsystem.model.response.ReceiveIdAndDepartmentIdResponse;
import fpt.capstone.buildingmanagementsystem.model.response.UserInfoResponse;
import fpt.capstone.buildingmanagementsystem.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserManageServiceTest {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    UserPendingRepository userPendingRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserPendingMapper userPendingMapper;
    @Autowired
    UserPendingStatusRepository userPendingStatusRepository;

    @Autowired
    UserRepositoryV2 userRepositoryV2;
    @Mock
    Logger logger;
    @Autowired
    UserManageService userManageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testChangeUserInfo() {
        String data = "{\n" +
                "    \"userId\": \"11dea336-8be4-4399-bce6-c57d510b5275\",\n" +
                "    \"firstName\": \"Sang\",\n" +
                "    \"lastName\": \"Son\",\n" +
                "    \"gender\": \"Nam\",\n" +
                "    \"dateOfBirth\": \"03/04/2002\",\n" +
                "    \"telephoneNumber\": \"0865965402\",\n" +
                "    \"country\": \"VietNam\",\n" +
                "    \"city\": \"Son Tay\",\n" +
                "    \"email\": \"sontung02hn@gmail.com\"\n" +
                "}";

        boolean result = userManageService.ChangeUserInfo(data, null);
        assertEquals(true, result);
    }

    @Test
    void testAcceptChangeUserInfo() {
        AcceptChangeUserInfo acceptChangeUserInfo = new AcceptChangeUserInfo();
        acceptChangeUserInfo.setUserId("11dea336-8be4-4399-bce6-c57d510b5275");

        boolean result = userManageService.AcceptChangeUserInfo(acceptChangeUserInfo);
        assertEquals(true, result);
    }

    @Test
    void testRejectChangeUserInfo() {
        GetUserInfoRequest getUserInfoRequest = new GetUserInfoRequest();
        getUserInfoRequest.setUserId("55fd796e-6e33-4b17-b6d9-d32aef9fce3f");

        boolean result = userManageService.RejectChangeUserInfo(getUserInfoRequest);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testGetAllUserNotVerify() {
        List<GetAllUserInfoPending> result = userManageService.getAllUserNotVerify();
        assertEquals(1, result.size());
    }

//    @Test
//    void testGetInfoUser() {
//        GetUserInfoRequest getUserInfoRequest = new GetUserInfoRequest();
//        getUserInfoRequest.setUserId("f2dbbf96-1a65-4e72-805d-ee10ca9b50a6");
//
//        GetUserInfoResponse result = userManageService.getInfoUser(getUserInfoRequest);
//        assertEquals(new GetUserInfoResponse("John", "Doe",
//                "Boyyyy", "03/04/2001",
//                "0865965402", "LA",
//                "LA", "sontung02hn@gmail.com", "unknown", "tech D1"), result);
//    }

    @Test
    void testGetAllUserInfo() {
        List<UserInfoResponse> result = userManageService.getAllUserInfo();
        assertEquals(8, result.size());
    }

    @Test
    void testGetReceiveIdAndDepartmentId() {
        String userId = "11dea336-8be4-4399-bce6-c57d510b5275";

        ReceiveIdAndDepartmentIdResponse result = userManageService.getReceiveIdAndDepartmentId(userId);
        assertEquals("11dea336-8be4-4399-bce6-c57d510b5275", result.getManagerInfoResponse().getManagerId());
        assertEquals("5", result.getManagerInfoResponse().getManagerDepartmentId());
        assertEquals("tech D3", result.getManagerInfoResponse().getManagerDepartmentName());

        assertEquals("3", result.getHrDepartmentResponse().getHrDepartmentId());
        assertEquals("human resources", result.getHrDepartmentResponse().getHrDepartmentName());
    }

    @Test
    void testGetManagerByDepartmentId() {
        List<UserInfoResponse> result = userManageService.getManagerByDepartmentId("2");
        assertEquals(1, result.size());
    }

//    @Test
//    void testGetUserAccount() {
//        List<UserAccountResponse> result = userManageService.getUserAccount("11dea336-8be4-4399-bce6-c57d510b5275");
//        assertEquals(1, result);
//    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme