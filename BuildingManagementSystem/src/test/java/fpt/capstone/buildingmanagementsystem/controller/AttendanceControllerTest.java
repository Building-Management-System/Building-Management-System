package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.model.request.AttendanceMessageRequest;
import fpt.capstone.buildingmanagementsystem.model.response.*;
import fpt.capstone.buildingmanagementsystem.service.AttendanceService;
import fpt.capstone.buildingmanagementsystem.service.RequestAttendanceFromService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class AttendanceControllerTest {
    @Autowired
    AttendanceService attendanceService;
    @Autowired
    RequestAttendanceFromService attendanceFromService;
    @Autowired
    AttendanceController attendanceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAttendanceUser() {
        String user_id = "";
        int month = 10;


        GetAttendanceUserResponse result = attendanceController.getAttendanceUser("user_id", 0);
        Assertions.assertEquals(new GetAttendanceUserResponse("username", "department", "date", new TotalAttendanceUser("date", 0, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d, 0d), List.of(new DailyLogResponse("dailyId", "dateDaily", null, null, 0d, 0d, 0d, true, true, 0d, 0d, true, 0d, 0d))), result);
    }

    @Test
    void testGetAttendanceUserDetail() {
        when(attendanceService.getAttendanceDetail(anyString(), anyString())).thenReturn(new AttendanceDetailResponse("date", new DailyDetailResponse("firstEntry", "lastExit", 0f, 0f, 0f, 0f, "dateType", 0f, 0f, 0f, "description"), new OverTimeDetailResponse("startTime", "endTime", "dateType", "manualStart", "manualEnd", "description")));

        AttendanceDetailResponse result = attendanceController.getAttendanceUserDetail("user_id", "date");
        Assertions.assertEquals(new AttendanceDetailResponse("date", new DailyDetailResponse("firstEntry", "lastExit", 0f, 0f, 0f, 0f, "dateType", 0f, 0f, 0f, "description"), new OverTimeDetailResponse("startTime", "endTime", "dateType", "manualStart", "manualEnd", "description")), result);
    }

    @Test
    void testAcceptAttendanceRequest() {
        AttendanceMessageRequest attendanceMessageRequest = new AttendanceMessageRequest();
        attendanceMessageRequest.setAttendanceRequestId("a0ac94f3-4a67-4ce6-a9a7-5ba193ad47ca");

        boolean result = attendanceController.acceptAttendanceRequest(attendanceMessageRequest);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testAcceptAttendanceRequest_NotFoundAttendanceID() {
        AttendanceMessageRequest attendanceMessageRequest = new AttendanceMessageRequest();
        attendanceMessageRequest.setAttendanceRequestId("not exist");

        BadRequest exception = org.junit.jupiter.api.Assertions.assertThrows(BadRequest.class,
                () -> attendanceController.acceptAttendanceRequest(attendanceMessageRequest));

        assertEquals("Not_found_attendance_id", exception.getMessage());
    }



    @Test
    void testRejectAttendanceRequest() {
        AttendanceMessageRequest attendanceMessageRequest =  new AttendanceMessageRequest();
        attendanceMessageRequest.setAttendanceRequestId("9c3b3609-355d-4f1f-83f1-134e967fa881");
        attendanceMessageRequest.setContent("reject");

        boolean result = attendanceController.rejectAttendanceRequest(attendanceMessageRequest);
        Assertions.assertEquals(true, result);
    }

    @Test
    void testRejectAttendanceRequest_NotFoundForm() {
        AttendanceMessageRequest attendanceMessageRequest =  new AttendanceMessageRequest();
        attendanceMessageRequest.setAttendanceRequestId("notexist");

        BadRequest exception = org.junit.jupiter.api.Assertions.assertThrows(BadRequest.class,
                () -> attendanceController.rejectAttendanceRequest(attendanceMessageRequest));
        assertEquals("Not_found_form", exception.getMessage());
    }
}
