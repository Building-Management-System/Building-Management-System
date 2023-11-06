package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.model.entity.ControlLogLcd;
import fpt.capstone.buildingmanagementsystem.model.entity.StrangerLogLcd;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.ControlLogStatus;
import fpt.capstone.buildingmanagementsystem.model.request.ControlLogAndStrangerLogSearchRequest;
import fpt.capstone.buildingmanagementsystem.model.response.*;
import fpt.capstone.buildingmanagementsystem.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.mockito.Mockito.*;

class SecurityServiceTest {
    @Mock
    ControlLogLcdRepository controlLogLcdRepository;
    @Mock
    StrangerLogLcdRepository strangerLogLcdRepository;
    @Mock
    AccountRepository accountRepository;
    @Mock
    RoomRepository roomRepository;
    @Mock
    DeviceRepository deviceRepository;
    @InjectMocks
    SecurityService securityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListAllDevice() {
        List<LoadDeviceResponse> result = securityService.listAllDevice();
        Assertions.assertEquals(List.of(new LoadDeviceResponse("deviceId", "deviceName")), result);
    }

    @Test
    void testGetListControlLogByDayAndDevice() throws ParseException {
        when(controlLogLcdRepository.getControlLogLcdListByDateAndDevice(any(), any(), anyString())).thenReturn(List.of(new ControlLogLcd("controlLogId", "operator", "personId", ControlLogStatus.WHITE_LIST, 0, 0, 0d, 0d, "persionName", "telnum", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 52).getTime(), 0d, 0d, new byte[]{(byte) 0}, null, null, null)));
        when(accountRepository.findByUsername(anyString())).thenReturn(null);

        List<ControlLogSecurityResponse> result = securityService.getListControlLogByDayAndDevice(new ControlLogAndStrangerLogSearchRequest("date", "startTime", "endTime", "deviceId"));
        Assertions.assertEquals(List.of(new ControlLogSecurityResponse("ControlLogId", new byte[]{(byte) 0}, "username", "firstName", "lastName", "department", "timeRecord", "verifyType", "room")), result);
    }

    @Test
    void testGetListStrangerLogByDayAndDevice() throws ParseException {
        when(strangerLogLcdRepository.getStrangerLogLcdListByDateAndDevice(any(), any(), anyString())).thenReturn(List.of(new StrangerLogLcd("strangerLogId", 0, "direction", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 52).getTime(), 0d, 0d, new byte[]{(byte) 0}, null, null)));

        List<StrangerLogSecurityResponse> result = securityService.getListStrangerLogByDayAndDevice(new ControlLogAndStrangerLogSearchRequest("date", "startTime", "endTime", "deviceId"));
        Assertions.assertEquals(List.of(new StrangerLogSecurityResponse("strangerLogId", 0, "deviceName", "deviceId", "room", "time", 0d, new byte[]{(byte) 0})), result);
    }

    @Test
    void testGetControlLogDetail() {
        when(controlLogLcdRepository.findByControlLogId(anyString())).thenReturn(null);
        when(accountRepository.findByUsername(anyString())).thenReturn(null);

        ControlLogDetailResponse result = securityService.getControlLogDetail("username", "controlLogId");
        Assertions.assertEquals(new ControlLogDetailResponse("avatar", "account", "role", "department", "hireDate", new byte[]{(byte) 0}, "deviceId", "deviceName", "time", 0d, "operator", "personId", "verifyStatus", 0d), result);
    }

    @Test
    void testListAllControlLogByStaff() {
        when(controlLogLcdRepository.getAll(anyString())).thenReturn(null);
        when(accountRepository.findAll()).thenReturn(List.of(null));

        List<ListAllControlLogByStaffResponse> result = securityService.listAllControlLogByStaff();
        Assertions.assertEquals(List.of(new ListAllControlLogByStaffResponse("username", "firstName", "lastName", "hireDate", "phone", "email", "gender", "verifyType")), result);
    }

    @Test
    void testGetListControlLogByAccount() {
        when(controlLogLcdRepository.findAllByPersionName(anyString())).thenReturn(null);

        ControlLogInfo result = securityService.getListControlLogByAccount("username");
        Assertions.assertEquals(new ControlLogInfo("avatar", "account", "role", "department", "hireDate", List.of(new ControlLogByAccountResponse("controlLogId", new byte[]{(byte) 0}, "account", "firstName", "lastName", "department", "timeRecord", "verifyType", "room"))), result);
    }
}
