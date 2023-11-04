package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.mapper.AccountMapper;
import fpt.capstone.buildingmanagementsystem.model.entity.*;
import fpt.capstone.buildingmanagementsystem.repository.*;
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

class InitializationServiceTest {
    @Mock
    AccountMapper accountMapper;
    @Mock
    RoomRepository roomRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    StatusRepository statusRepository;
    @Mock
    UserPendingStatusRepository userPendingStatusRepository;
    @Mock
    DepartmentRepository departmentRepository;
    @Mock
    AccountRepository accountRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    DayOffRepository dayOffRepository;
    @Mock
    DeviceRepository deviceRepository;
    @InjectMocks
    InitializationService initializationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInit() {
        when(accountMapper.convertRegisterAccount(any(), any(), any())).thenReturn(new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 16).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 16).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")));
        when(roomRepository.findAll()).thenReturn(List.of(new Room(0, "roomName", null)));
        when(roleRepository.findAll()).thenReturn(List.of(new Role("roleId", "roleName")));
        when(statusRepository.findAll()).thenReturn(List.of(new Status("statusId", "statusName")));
        when(userPendingStatusRepository.findAll()).thenReturn(List.of(new UserPendingStatus("userPendingStatusId", "userPendingStatusName")));
        when(departmentRepository.findAll()).thenReturn(List.of(new Department("departmentId", "departmentName")));
        when(accountRepository.findAll()).thenReturn(List.of(new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 16).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 16).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName"))));

        initializationService.init();
    }

    @Test
    void testInitEmployeeDayOff() {
        when(accountRepository.findAll()).thenReturn(List.of(new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 16).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 16).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName"))));

        List<DayOff> result = initializationService.initEmployeeDayOff();
        Assertions.assertEquals(List.of(new DayOff("dayOffId", 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0, new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 16).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 16).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")))), result);
    }
}
//pass