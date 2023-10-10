package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.model.entity.Department;
import fpt.capstone.buildingmanagementsystem.model.entity.DeviceAccount;
import fpt.capstone.buildingmanagementsystem.model.entity.Room;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.ControlLogStatus;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.DeviceStatus;
import fpt.capstone.buildingmanagementsystem.model.request.*;
import fpt.capstone.buildingmanagementsystem.model.response.AccountLcdResponse;
import fpt.capstone.buildingmanagementsystem.model.response.DeviceAccountResponse;
import fpt.capstone.buildingmanagementsystem.model.response.DeviceRoomResponse;
import fpt.capstone.buildingmanagementsystem.model.response.RoomResponse;
import fpt.capstone.buildingmanagementsystem.repository.AccountRepository;
import fpt.capstone.buildingmanagementsystem.repository.DeviceAccountRepository;
import fpt.capstone.buildingmanagementsystem.repository.DeviceRepository;
import fpt.capstone.buildingmanagementsystem.repository.RoomRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.mockito.Mockito.*;

class DeviceServiceTest {
    @Mock
    AccountRepository accountRepository;
    @Mock
    DeviceAccountRepository deviceAccountRepository;
    @Mock
    DeviceRepository deviceRepository;
    @Mock
    RoomRepository roomRepository;
    @InjectMocks
    DeviceService deviceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDevice() {
        when(roomRepository.findAll()).thenReturn(List.of(new Room(0, "roomName", null)));

        List<DeviceRoomResponse> result = deviceService.getAllDevice();
        Assertions.assertEquals(List.of(new DeviceRoomResponse(0, "roomName", "deviceId", "lcdId", "deviceName", DeviceStatus.ACTIVE, "deviceUrl", "deviceNote", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime())), result);
    }

    @Test
    void testUpdateDevice() {
        when(roomRepository.getRoomByInActiveDevice(anyInt())).thenReturn(List.of(new Room(0, "roomName", null)));

        ResponseEntity<?> result = deviceService.updateDevice(new DeviceRoomRequest("deviceId", "newRoomId", "deviceName", "deviceLcdId", "deviceUrl"));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testUpdateDeviceStatus() {
        ResponseEntity<?> result = deviceService.updateDeviceStatus(new DeviceStatusRequest("id", DeviceStatus.ACTIVE, "deviceNote"));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testDeleteDevice() {
        when(deviceAccountRepository.findByDevice(any())).thenReturn(List.of(new DeviceAccount("deviceAccountId", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime(), ControlLogStatus.WHITE_LIST, null, null)));
        when(roomRepository.getRoomByDevice(anyString())).thenReturn(List.of(new Room(0, "roomName", null)));

        ResponseEntity<?> result = deviceService.deleteDevice(new DeviceRequest("deviceId"));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testGetDeviceDetail() {
        when(deviceAccountRepository.findByDevice(any())).thenReturn(List.of(new DeviceAccount("deviceAccountId", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime(), ControlLogStatus.WHITE_LIST, null, null)));
        when(roomRepository.getRoomByDevice(anyString())).thenReturn(List.of(new Room(0, "roomName", null)));

        DeviceAccountResponse result = deviceService.getDeviceDetail("deviceId");
        Assertions.assertEquals(new DeviceAccountResponse("deviceId", "deviceName", "deviceLcdId", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime(), DeviceStatus.ACTIVE, List.of(new RoomResponse(0, "roomName")), List.of(new AccountLcdResponse("deviceAccountId", "accountId", "userName", "firstName", "lastName", new Department("departmentId", "departmentName"), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime(), ControlLogStatus.WHITE_LIST, "messageSetupMqtt"))), result);
    }

    @Test
    void testRegisterNewAccount() {
        when(deviceAccountRepository.findByDeviceAndAccount(any(), any())).thenReturn(List.of(new DeviceAccount("deviceAccountId", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime(), ControlLogStatus.WHITE_LIST, null, null)));

        ResponseEntity<?> result = deviceService.registerNewAccount(new AccountDeviceRequest("accountId", "roomIdString", "startDate", "endDate"));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testChangeRecordStatus() {
        ResponseEntity<?> result = deviceService.changeRecordStatus(new ChangeRecordStatusRequest("deviceAccountId", ControlLogStatus.WHITE_LIST));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testChangeAccountStatus() {
        when(deviceAccountRepository.findByAccount(any())).thenReturn(List.of(new DeviceAccount("deviceAccountId", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime(), ControlLogStatus.WHITE_LIST, null, null)));

        boolean result = deviceService.changeAccountStatus(new ChangeStatusRequest("accountId", ControlLogStatus.WHITE_LIST));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testDeleteAccountDevice() {
        when(accountRepository.findByAccountId(anyString())).thenReturn(null);
        when(deviceAccountRepository.findByAccount(any())).thenReturn(List.of(new DeviceAccount("deviceAccountId", new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 19, 22, 58).getTime(), ControlLogStatus.WHITE_LIST, null, null)));

        deviceService.deleteAccountDevice("accountId");
    }

    @Test
    void testMessageSetupMqtt() {
        String result = deviceService.messageSetupMqtt("accountId", "startDate", "endDate", "deviceLcdId");
        Assertions.assertEquals("replaceMeWithExpectedResult", result);
    }

    @Test
    void testMessageEditPerson() {
        String result = deviceService.messageEditPerson("lcdDeviceId", "accountId", 0);
        Assertions.assertEquals("replaceMeWithExpectedResult", result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme