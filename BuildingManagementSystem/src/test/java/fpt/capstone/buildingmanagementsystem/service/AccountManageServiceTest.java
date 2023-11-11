package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.mapper.AccountMapper;
import fpt.capstone.buildingmanagementsystem.model.entity.*;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.DateType;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.RequestStatus;
import fpt.capstone.buildingmanagementsystem.model.request.*;
import fpt.capstone.buildingmanagementsystem.model.response.GetAllAccountResponse;
import fpt.capstone.buildingmanagementsystem.repository.*;
import fpt.capstone.buildingmanagementsystem.security.PasswordEncode;
import fpt.capstone.buildingmanagementsystem.until.EmailSender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

class AccountManageServiceTest {
    @Autowired
    DailyLogRepository dailyLogRepository;
    @Autowired
    OverTimeRepository overTimeRepository;
    @Autowired
    ChatMessageRepository chatMessageRepository;
    @Autowired
    RequestTicketRepository requestTicketRepository;
    @Autowired
    RequestMessageRepository requestMessageRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    EmailSender emailSender;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncode passwordEncode;
    @Autowired
    AccountMapper accountMapper;
    @Autowired
    StatusRepository statusRepository;
    @Autowired
    UserRepository userRepository;
    @InjectMocks
    AccountManageService accountManageService;

    @Autowired
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoadUserByUsername() {
        when(accountRepository.findByUsername(anyString())).thenReturn(null);
        when(roleRepository.findByRoleId(anyString())).thenReturn(null);

        UserDetails result = accountManageService.loadUserByUsername("username");
        Assertions.assertEquals(null, result);
    }

    @Test
    void testSaveNewAccount() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("emptech1");
        registerRequest.setPassword("123");
        registerRequest.setRole("employee");
        registerRequest.setDepartmentName("tech D1");
        registerRequest.setHrId("94b38a94-bb4a-4adb-acc9-34bde9babd4e");

        boolean result = accountManageService.saveNewAccount(registerRequest);

        assertTrue(result);

    }

    @Test
    void testChangePassword() {
        when(accountRepository.findByUsername(anyString())).thenReturn(null);
        when(accountRepository.findByAccountId(anyString())).thenReturn(null);
        when(accountRepository.existsByUsername(anyString())).thenReturn(Boolean.TRUE);
        when(accountRepository.updatePassword(anyString(), any(), anyString())).thenReturn(0);
        when(passwordEncode.passwordEncoder()).thenReturn(null);

        boolean result = accountManageService.changePassword(new ChangePasswordRequest("accountId", "oldPassword", "newPassword"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testCheckUsernameAndPassword() {
        when(accountRepository.findByUsername(anyString())).thenReturn(null);
        when(accountRepository.existsByUsername(anyString())).thenReturn(Boolean.TRUE);
        when(passwordEncode.passwordEncoder()).thenReturn(null);

        boolean result = accountManageService.checkUsernameAndPassword("username", "password");
        Assertions.assertEquals(true, result);
    }

    @Test
    void testChangeStatusAccount() {
        when(accountRepository.findByAccountId(anyString())).thenReturn(null);
        when(accountRepository.updateStatusAccount(anyString(), anyString())).thenReturn(0);
        when(statusRepository.findByStatusName(anyString())).thenReturn(null);

        boolean result = accountManageService.changeStatusAccount(new ChangeStatusAccountRequest("accountId", "statusName"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testChangeRoleAccount() {
        when(departmentRepository.findByDepartmentId(anyString())).thenReturn(null);
        when(departmentRepository.findByDepartmentName(anyString())).thenReturn(null);
        when(accountRepository.findByAccountId(anyString())).thenReturn(null);
        when(accountRepository.updateRoleAccount(anyString(), anyString())).thenReturn(0);
        when(accountRepository.updateDepartmentUser(anyString(), anyString())).thenReturn(0);
        when(roleRepository.findByRoleName(anyString())).thenReturn(null);
        when(userRepository.findAllByDepartment(any())).thenReturn(List.of(new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "country", "city", "email", "image", new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), "createdBy", null, null, null), new Department("departmentId", "departmentName"))));

        boolean result = accountManageService.changeRoleAccount(new ChangeRoleRequest("accountId", "roleName", "departmentId"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testResetPassword() {
        when(accountRepository.findByUsername(anyString())).thenReturn(null);
        when(accountRepository.existsByUsername(anyString())).thenReturn(Boolean.TRUE);
        when(accountRepository.updatePassword(anyString(), any(), anyString())).thenReturn(0);
        when(passwordEncode.passwordEncoder()).thenReturn(null);

        boolean result = accountManageService.resetPassword(new ResetPasswordRequest("username"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testGetGettingRole() {

    }

    @Test
    void testGetGetAllAccount() {
        when(accountRepository.findAll()).thenReturn(List.of(new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), "createdBy", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "country", "city", "email", "image", new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), null, new Department("departmentId", "departmentName")), null, null)));
        when(accountMapper.convertGetAllAccount(any())).thenReturn(new GetAllAccountResponse("accountId", "username", "roleName", "statusName", "statusId", "createdBy"));

        List<GetAllAccountResponse> result = accountManageService.getGetAllAccount();
        Assertions.assertEquals(List.of(new GetAllAccountResponse("accountId", "username", "roleName", "statusName", "statusId", "createdBy")), result);
    }

    @Test
    void testGetAccountId() {
        when(accountRepository.findByUsername(anyString())).thenReturn(null);

        String result = accountManageService.getAccountId("username");
        Assertions.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    void testDeleteAccount() {
        when(dailyLogRepository.findAllByUser(any())).thenReturn(List.of(new DailyLog("dailyId", null, null, null, 0f, 0f, 0f, 0f, DateType.NORMAL, 0f, 0f, 0f, "description", null)));
        when(overTimeRepository.findAllByUser(any())).thenReturn(List.of(new OvertimeLog("otId", null, null, null, DateType.NORMAL, null, null, "description", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "country", "city", "email", "image", new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), "createdBy", null, null, null), new Department("departmentId", "departmentName")))));
        when(chatMessageRepository.findAllByReceiver(any())).thenReturn(List.of(new ChatMessage("id", null, null, "message", new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), "status", "type")));
        when(chatMessageRepository.findAllBySender(any())).thenReturn(List.of(new ChatMessage("id", null, null, "message", new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), "status", "type")));
        when(requestTicketRepository.findAllByUser(any())).thenReturn(List.of(new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), RequestStatus.PENDING, null, null)));
        when(requestMessageRepository.findAllByReceiver(any())).thenReturn(List.of(new RequestMessage("requestMessageId", new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), "attachmentMessageId", null, null, new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), RequestStatus.PENDING, null, null), new Department("departmentId", "departmentName"))));
        when(requestMessageRepository.findAllBySender(any())).thenReturn(List.of(new RequestMessage("requestMessageId", new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), "attachmentMessageId", null, null, new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), RequestStatus.PENDING, null, null), new Department("departmentId", "departmentName"))));
        when(accountRepository.findByUsername(anyString())).thenReturn(null);

        boolean result = accountManageService.deleteAccount("username", "hrId");
        Assertions.assertEquals(true, result);
    }

    @Test
    void testGetGettingRole2() {

    }

    @Test
    void testCheckManagerOfDepartment() {
        when(departmentRepository.findByDepartmentName(anyString())).thenReturn(null);
        when(accountRepository.findByAccountId(anyString())).thenReturn(null);
        when(userRepository.findAllByDepartment(any())).thenReturn(List.of(new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "country", "city", "email", "image", new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 7, 21, 43).getTime(), "createdBy", null, null, null), new Department("departmentId", "departmentName"))));

        boolean result = accountManageService.checkManagerOfDepartment("departmentName");
        Assertions.assertEquals(true, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme