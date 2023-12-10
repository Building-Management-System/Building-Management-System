package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.entity.Department;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.ControlLogStatus;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.DeviceStatus;
import fpt.capstone.buildingmanagementsystem.model.request.AccountDeviceRequest;
import fpt.capstone.buildingmanagementsystem.model.request.DeviceRoomRequest;
import fpt.capstone.buildingmanagementsystem.model.request.DeviceStatusRequest;
import fpt.capstone.buildingmanagementsystem.model.response.AccountLcdResponse;
import fpt.capstone.buildingmanagementsystem.model.response.DeviceAccountResponse;
import fpt.capstone.buildingmanagementsystem.model.response.DeviceRoomResponse;
import fpt.capstone.buildingmanagementsystem.model.response.RoomResponse;
import fpt.capstone.buildingmanagementsystem.service.DeviceService;
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

class DeviceControllerTest {
    @Mock
    DeviceService deviceService;
    @InjectMocks
    DeviceController deviceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDevice() {
        when(deviceService.getAllDevice()).thenReturn(List.of(new DeviceRoomResponse(0, "roomName", "deviceId", "lcdId", "deviceName", DeviceStatus.ACTIVE, "deviceUrl", "deviceNote", new GregorianCalendar(2023, Calendar.DECEMBER, 10, 16, 26).getTime())));

        List<DeviceRoomResponse> result = deviceController.getAllDevice();
        Assertions.assertEquals(List.of(new DeviceRoomResponse(0, "roomName", "deviceId", "lcdId", "deviceName", DeviceStatus.ACTIVE, "deviceUrl", "deviceNote", new GregorianCalendar(2023, Calendar.DECEMBER, 10, 16, 26).getTime())), result);
    }

    @Test
    void testCreateDeviceAccount() {
        when(deviceService.registerNewAccount(any())).thenReturn(null);

        ResponseEntity<?> result = deviceController.createDeviceAccount(new AccountDeviceRequest("accountId", "roomIdString", "startDate", "endDate"));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testUpdateDevice() {
        when(deviceService.updateDevice(any())).thenReturn(null);

        ResponseEntity<?> result = deviceController.updateDevice(new DeviceRoomRequest("deviceId", 0, "deviceName", "deviceLcdId", "deviceUrl"));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testUpdateDeviceStatus() {
        when(deviceService.updateDeviceStatus(any())).thenReturn(null);

        ResponseEntity<?> result = deviceController.updateDeviceStatus(new DeviceStatusRequest("id", DeviceStatus.ACTIVE, "deviceNote"));
        Assertions.assertEquals(null, result);
    }

    @Test
    void testGetDeviceDetail() {
        when(deviceService.getDeviceDetail(anyString())).thenReturn(new DeviceAccountResponse("deviceId", "deviceName", "deviceLcdId", new GregorianCalendar(2023, Calendar.DECEMBER, 10, 16, 26).getTime(), DeviceStatus.ACTIVE, List.of(new RoomResponse(0, "roomName")), List.of(new AccountLcdResponse("accountId", "userName", "firstName", "lastName", new Department("departmentId", "departmentName"), new GregorianCalendar(2023, Calendar.DECEMBER, 10, 16, 26).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 10, 16, 26).getTime(), ControlLogStatus.WHITE_LIST))));

        DeviceAccountResponse result = deviceController.getDeviceDetail("deviceId");
        Assertions.assertEquals(new DeviceAccountResponse("deviceId", "deviceName", "deviceLcdId", new GregorianCalendar(2023, Calendar.DECEMBER, 10, 16, 26).getTime(), DeviceStatus.ACTIVE, List.of(new RoomResponse(0, "roomName")), List.of(new AccountLcdResponse("accountId", "userName", "firstName", "lastName", new Department("departmentId", "departmentName"), new GregorianCalendar(2023, Calendar.DECEMBER, 10, 16, 26).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 10, 16, 26).getTime(), ControlLogStatus.WHITE_LIST))), result);
    }
}
