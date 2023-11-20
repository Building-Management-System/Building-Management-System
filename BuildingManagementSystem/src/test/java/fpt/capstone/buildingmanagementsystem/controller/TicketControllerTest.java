package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.request.ChangeReceiveIdRequest;
import fpt.capstone.buildingmanagementsystem.model.response.TicketRequestResponseV2;
import fpt.capstone.buildingmanagementsystem.service.TicketManageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TicketControllerTest {
    @Autowired
    TicketManageService ticketManageService;
    @Autowired
    TicketController ticketController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    void testGetAllTicketAndRequest() {
//        List<TicketRequestResponse> result = ticketController.getAllTicketAndRequest();
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//        assertEquals(1, result.size());
//    }

    @Test
    void testGetAllTicketAndRequest2() {
        String senderID = "f2dbbf96-1a65-4e72-805d-ee10ca9b50a6";

        List<TicketRequestResponseV2> result = ticketController.getAllTicketAndRequest(senderID);
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void testGetAllTicketAndRequestByHr() {
        List<TicketRequestResponseV2> result = ticketController.getAllTicketAndRequestByHr();
        assertNotNull(result);
        assertEquals(2, result);
    }

    @Test
    void testGetAllTicketAndRequestBySecurity() {
        when(ticketManageService.getAllTicketsBySecurity()).thenReturn(List.of(new TicketRequestResponseV2()));

        List<TicketRequestResponseV2> result = ticketController.getAllTicketAndRequestBySecurity();
        Assertions.assertEquals(List.of(new TicketRequestResponseV2()), result);
    }

    @Test
    void testGetAllTicketAndRequestByAdmin() {
        when(ticketManageService.getAllTicketsByAdmin()).thenReturn(List.of(new TicketRequestResponseV2()));

        List<TicketRequestResponseV2> result = ticketController.getAllTicketAndRequestByAdmin();
        Assertions.assertEquals(List.of(new TicketRequestResponseV2()), result);
    }

    @Test
    void testGetAllTicketAndRequestByDepartmentManager() {
        when(ticketManageService.getAllTicketByDepartmentManager(anyString())).thenReturn(List.of(new TicketRequestResponseV2()));

        List<TicketRequestResponseV2> result = ticketController.getAllTicketAndRequestByDepartmentManager("departmentName");
        Assertions.assertEquals(List.of(new TicketRequestResponseV2()), result);
    }

    @Test
    void testChangeReceiveIdRequest() {
        when(ticketManageService.changeReceiveId(any())).thenReturn(true);

        boolean result = ticketController.changeReceiveIdRequest(new ChangeReceiveIdRequest("requestId", "receiverId"));
        Assertions.assertEquals(true, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme