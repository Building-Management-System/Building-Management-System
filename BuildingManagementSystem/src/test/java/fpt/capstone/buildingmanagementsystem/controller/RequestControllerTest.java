package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.request.*;
import fpt.capstone.buildingmanagementsystem.model.response.ReceiveIdAndDepartmentIdResponse;
import fpt.capstone.buildingmanagementsystem.service.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class  RequestControllerTest {
    @Autowired
    RequestOtherService requestOtherService;
    @Autowired
    RequestAttendanceFromService requestAttendanceFromService;
    @Autowired
    RequestLeaveFormService requestLeaveFormService;
    @Autowired
    UserManageService userManageService;

    @Autowired
    RoomBookingService roomBookingService;
    @Autowired
    RequestController requestController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetReceiveIdAndDepartmentId() {
        GetUserInfoRequest getUserId = new GetUserInfoRequest();
        getUserId.setUserId("11dea336-8be4-4399-bce6-c57d510b5275");

        ReceiveIdAndDepartmentIdResponse result = requestController.getReceiveIdAndDepartmentId(getUserId);
        assertEquals("11dea336-8be4-4399-bce6-c57d510b5275", result.getManagerInfoResponse().getManagerId());
        assertEquals("5", result.getManagerInfoResponse().getManagerDepartmentId());
        assertEquals("tech D3", result.getManagerInfoResponse().getManagerDepartmentName());

        assertEquals("3", result.getHrDepartmentResponse().getHrDepartmentId());
        assertEquals("human resources", result.getHrDepartmentResponse().getHrDepartmentName());
    }

    @Test
    void testRequestAttendanceForm() {
        SendAttendanceFormRequest sendAttendanceFormRequest = new SendAttendanceFormRequest();
        sendAttendanceFormRequest.setUserId("f2dbbf96-1a65-4e72-805d-ee10ca9b50a6");
        sendAttendanceFormRequest.setTitle("attendance khong content");
        sendAttendanceFormRequest.setContent("");
        sendAttendanceFormRequest.setDepartmentId("2");
        sendAttendanceFormRequest.setManualDate("2023-10-20");
        sendAttendanceFormRequest.setManualFirstEntry("08:00:00");
        sendAttendanceFormRequest.setManualLastExit("19:00:00");
        sendAttendanceFormRequest.setReceivedId("94b38a94-bb4a-4adb-acc9-34bde9babd4e");

        boolean result = requestController.requestAttendanceForm(sendAttendanceFormRequest);
        assertEquals(true, result);
    }

    @Test
    void testRequestAttendanceFormExistTicket() {
        SendAttendanceFormRequest sendAttendanceFormRequest = new SendAttendanceFormRequest();
        sendAttendanceFormRequest.setUserId("f2dbbf96-1a65-4e72-805d-ee10ca9b50a6");
        sendAttendanceFormRequest.setTitle("attendance reesut 2 v3");
        sendAttendanceFormRequest.setTicketId("AT_37aacd33-d892-489e-ac37-478ee9ff5512");
        sendAttendanceFormRequest.setContent("sua nhanhn len");
        sendAttendanceFormRequest.setManualDate("2023-10-18");
        sendAttendanceFormRequest.setManualFirstEntry("08:00:00");
        sendAttendanceFormRequest.setManualLastExit("19:00:00");
        sendAttendanceFormRequest.setReceivedId("94b38a94-bb4a-4adb-acc9-34bde9babd4e");
        sendAttendanceFormRequest.setDepartmentId("2");


        boolean result = requestController.requestAttendanceFormExistTicket(sendAttendanceFormRequest);
        assertEquals(true, result);
    }

    @Test
    void testRequestAttendanceFormExistRequest() {
        SendAttendanceFormRequest sendAttendanceFormRequest = new SendAttendanceFormRequest();
        sendAttendanceFormRequest.setRequestId("AT_9319bff1-3756-4412-9c72-12feff93c239");
        sendAttendanceFormRequest.setUserId("f2dbbf96-1a65-4e72-805d-ee10ca9b50a6");
        sendAttendanceFormRequest.setTitle("anon caiutay");
        sendAttendanceFormRequest.setContent("thua mu thoi");
        sendAttendanceFormRequest.setDepartmentId("2");
        sendAttendanceFormRequest.setManualDate("2023-10-21");
        sendAttendanceFormRequest.setManualFirstEntry("08:00:00");
        sendAttendanceFormRequest.setManualLastExit("22:00:00");
        sendAttendanceFormRequest.setReceivedId("94b38a94-bb4a-4adb-acc9-34bde9babd4e");

        boolean result = requestController.requestAttendanceFormExistRequest(sendAttendanceFormRequest);
        assertEquals(true, result);
    }

    @Test
    void testRequestLeaveForm() {
        SendLeaveFormRequest sendLeaveFormRequest = new SendLeaveFormRequest();
        sendLeaveFormRequest.setUserId("55fd796e-6e33-4b17-b6d9-d32aef9fce3f");
        sendLeaveFormRequest.setTitle("leave part3");
        sendLeaveFormRequest.setContent("leave part3");
        sendLeaveFormRequest.setFromDate("2023-11-15");
        sendLeaveFormRequest.setToDate("2023-11-18");
        sendLeaveFormRequest.setHalfDay(true);
        sendLeaveFormRequest.setDurationEvaluation(1);
        sendLeaveFormRequest.setDepartmentId("2");
        sendLeaveFormRequest.setReceivedId("f2dbbf96-1a65-4e72-805d-ee10ca9b50a6");

        boolean result = requestController.requestLeaveForm(sendLeaveFormRequest);
        assertEquals(true, result);
    }

    @Test
    void testRequestLeaveFormUserExistTicket() {
        SendLeaveFormRequest sendLeaveFormRequest = new SendLeaveFormRequest();
        sendLeaveFormRequest.setUserId("55fd796e-6e33-4b17-b6d9-d32aef9fce3f");
        sendLeaveFormRequest.setTicketId("LV_9d22938f-c16e-46d0-af8e-eb44dc25c9e6");
        sendLeaveFormRequest.setTitle("hnay t nghi that dayyyy");
        sendLeaveFormRequest.setContent("t lay mayyyyy code di");
        sendLeaveFormRequest.setFromDate("2023-11-13");
        sendLeaveFormRequest.setToDate("2023-11-13");
        sendLeaveFormRequest.setHalfDay(true);
        sendLeaveFormRequest.setDurationEvaluation(1);
        sendLeaveFormRequest.setDepartmentId("2");
        sendLeaveFormRequest.setReceivedId("f2dbbf96-1a65-4e72-805d-ee10ca9b50a6");

        boolean result = requestController.requestLeaveFormUserExistTicket(sendLeaveFormRequest);
        assertEquals(true, result);
    }

    @Test
    void testRequestLeaveFormUserExistRequest() {
        SendLeaveFormRequest sendLeaveFormRequest = new SendLeaveFormRequest();
        sendLeaveFormRequest.setUserId("55fd796e-6e33-4b17-b6d9-d32aef9fce3f");
        sendLeaveFormRequest.setRequestId("LV_c5b73249-b1c1-4b44-bb07-7dbf44b05ad8");
        sendLeaveFormRequest.setContent("adasd siahf oaub ỉyhbgs íu fiub sg sguh sth g íh ");
        sendLeaveFormRequest.setFromDate("2023-11-12");
        sendLeaveFormRequest.setToDate("2023-11-13");
        sendLeaveFormRequest.setDepartmentId("2");

        boolean result = requestController.requestLeaveFormUserExistRequest(sendLeaveFormRequest);
        assertEquals(true, result);
    }

    @Test
    void testRequestRoomBookingForm() {
        SendRoomBookingRequest sendRoomBookingRequest = new SendRoomBookingRequest();
        sendRoomBookingRequest.setUserId("0dff5d5c-095d-4386-91f2-82bdb7eba342");
        sendRoomBookingRequest.setTitle("Metting bamasd");
        sendRoomBookingRequest.setContent("sinh nhat a sang");
        sendRoomBookingRequest.setBookingDate("2023-11-12");
        sendRoomBookingRequest.setStartTime("10:00:00");
        sendRoomBookingRequest.setEndTime("10:30:00");
        sendRoomBookingRequest.setDepartmentReceiverId("8");
        sendRoomBookingRequest.setDepartmentSenderId("2");
        sendRoomBookingRequest.setRoomId(4);

        boolean result = requestController.requestRoomBookingForm(sendRoomBookingRequest);
        assertEquals(true, result);
    }

    @Test
    void testRequestRoomBookingFormExistTicket() {
        SendRoomBookingRequest sendRoomBookingRequest = new SendRoomBookingRequest();
        sendRoomBookingRequest.setUserId("0dff5d5c-095d-4386-91f2-82bdb7eba342");
        sendRoomBookingRequest.setTicketId("RB_fcd105eb-f358-47c6-843a-951d8ac0a98d");
        sendRoomBookingRequest.setTitle("Metting sinh nhat 2");
        sendRoomBookingRequest.setContent("sinh nhat a sang");
        sendRoomBookingRequest.setBookingDate("2023-11-11");
        sendRoomBookingRequest.setStartTime("13:00:00");
        sendRoomBookingRequest.setEndTime("14:00:00");
        sendRoomBookingRequest.setDepartmentReceiverId("8");
        sendRoomBookingRequest.setDepartmentSenderId("2");
        sendRoomBookingRequest.setRoomId(6);

        boolean result = requestController.requestRoomBookingFormExistTicket(sendRoomBookingRequest);
        assertEquals(true, result);
    }

    @Test
    void testRequestRoomBookingFormExistRequest() {
        SendRoomBookingRequest sendRoomBookingRequest = new SendRoomBookingRequest();
        sendRoomBookingRequest.setUserId("0dff5d5c-095d-4386-91f2-82bdb7eba342");
        sendRoomBookingRequest.setRequestId("RB_7a58fdb8-3875-44dd-a1e4-c332ee6d36c3");
        sendRoomBookingRequest.setContent("sinh nhat a sang");
        sendRoomBookingRequest.setBookingDate("2023-11-13");
        sendRoomBookingRequest.setStartTime("11:00:00");
        sendRoomBookingRequest.setEndTime("12:00:00");
        sendRoomBookingRequest.setDepartmentReceiverId("8");
        sendRoomBookingRequest.setDepartmentSenderId("2");
        sendRoomBookingRequest.setRoomId(6);

        boolean result = requestController.requestRoomBookingFormExistRequest(sendRoomBookingRequest);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testRequestOtherForm() {
        SendOtherFormRequest sendOtherFormRequest = new SendOtherFormRequest();
        sendOtherFormRequest.setUserId("55fd796e-6e33-4b17-b6d9-d32aef9fce3f");
        sendOtherFormRequest.setReceivedId("f2dbbf96-1a65-4e72-805d-ee10ca9b50a6");
        sendOtherFormRequest.setContent("other request 1");
        sendOtherFormRequest.setDepartmentId("2");
        sendOtherFormRequest.setTitle("other request 1");

        boolean result = requestController.requestOtherForm(sendOtherFormRequest);
        assertEquals(true, result);
    }

    @Test
    void testRequestOtherFormExistTicket() {
        SendOtherFormRequest sendOtherFormRequest = new SendOtherFormRequest();
        sendOtherFormRequest.setTicketId("OR_116365ad-2b2b-4daf-97b0-7a30a1875785");
        sendOtherFormRequest.setUserId("55fd796e-6e33-4b17-b6d9-d32aef9fce3f");
        sendOtherFormRequest.setReceivedId("f8dbabf3-34d5-4b19-97dd-d99d7b34e11f");
        sendOtherFormRequest.setContent("benxema");
        sendOtherFormRequest.setDepartmentId("2");
        sendOtherFormRequest.setTitle("important thing 2 update");

        boolean result = requestController.requestOtherFormExistTicket(sendOtherFormRequest);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testRequestOtherFormExistRequest() {
        SendOtherFormRequest sendOtherFormRequest = new SendOtherFormRequest();
        sendOtherFormRequest.setRequestId("OR_8c58fcbb-240e-413b-9eb0-e02a8d02aefe");
        sendOtherFormRequest.setUserId("55fd796e-6e33-4b17-b6d9-d32aef9fce3f");
        sendOtherFormRequest.setReceivedId("f8dbabf3-34d5-4b19-97dd-d99d7b34e11f");
        sendOtherFormRequest.setContent("sdhb siubf sdifb nonononon");
        sendOtherFormRequest.setDepartmentId("2");

        boolean result = requestController.requestOtherFormExistRequest(sendOtherFormRequest);
        Assertions.assertEquals(true, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme