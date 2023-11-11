package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.model.dto.TicketDto;
import fpt.capstone.buildingmanagementsystem.model.dto.TicketRequestDto;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.request.ChangeReceiveIdRequest;
import fpt.capstone.buildingmanagementsystem.model.response.TicketRequestResponse;
import fpt.capstone.buildingmanagementsystem.model.response.TicketRequestResponseV2;
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

class TicketManageServiceTest {
    @Mock
    TicketRepositoryv2 ticketRepositoryv2;
    @Mock
    UserRepository userRepository;
    @Mock
    TicketRepository ticketRepository;
    @Mock
    RequestTicketRepository requestTicketRepository;
    @Mock
    RequestMessageRepository requestMessageRepository;
    @InjectMocks
    TicketManageService ticketManageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTickets() {
        when(ticketRepositoryv2.getAllTicketRequest()).thenReturn(List.of(new TicketDto()));

        List<TicketRequestResponse> result = ticketManageService.getAllTickets();
        Assertions.assertEquals(List.of(new TicketRequestResponse()), result);
    }

    @Test
    void testGetAllTicketsBySenderId() {
        when(ticketRepositoryv2.getTicketRequestv2()).thenReturn(List.of(new TicketRequestDto()));

        List<TicketRequestResponseV2> result = ticketManageService.getAllTicketsBySenderId("senderId");
        Assertions.assertEquals(List.of(new TicketRequestResponseV2()), result);
    }

    @Test
    void testGetAllTicketsByHr() {
        when(ticketRepositoryv2.getTicketRequestByHr()).thenReturn(List.of(new TicketRequestDto()));

        List<TicketRequestResponseV2> result = ticketManageService.getAllTicketsByHr();
        Assertions.assertEquals(List.of(new TicketRequestResponseV2()), result);
    }

    @Test
    void testGetAllTicketsBySecurity() {
        when(ticketRepositoryv2.getTicketRequestBySecurity()).thenReturn(List.of(new TicketRequestDto()));

        List<TicketRequestResponseV2> result = ticketManageService.getAllTicketsBySecurity();
        Assertions.assertEquals(List.of(new TicketRequestResponseV2()), result);
    }

    @Test
    void testGetAllTicketsByAdmin() {
        when(ticketRepositoryv2.getTicketRequestByAdmin()).thenReturn(List.of(new TicketRequestDto()));

        List<TicketRequestResponseV2> result = ticketManageService.getAllTicketsByAdmin();
        Assertions.assertEquals(List.of(new TicketRequestResponseV2()), result);
    }

    @Test
    void testGetAllTicketByDepartmentManager() {
        when(ticketRepositoryv2.getTicketRequestByDepartmentManager(anyString())).thenReturn(List.of(new TicketRequestDto()));
        when(userRepository.getManagerByDepartment(anyString())).thenReturn(List.of(new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "country", "city", "email", "image", new GregorianCalendar(2023, Calendar.NOVEMBER, 8, 23, 29).getTime(), new GregorianCalendar(2023, Calendar.NOVEMBER, 8, 23, 29).getTime(), null, null)));

        List<TicketRequestResponseV2> result = ticketManageService.getAllTicketByDepartmentManager("departmentName");
        Assertions.assertEquals(List.of(new TicketRequestResponseV2()), result);
    }

    @Test
    void testChangeReceiveId() {
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        when(ticketRepository.updateTicketTime(any(), anyString())).thenReturn(0);
        when(requestTicketRepository.findByRequestId(anyString())).thenReturn(null);
        when(requestMessageRepository.updateTicketRequestTime(anyString(), any(), anyString())).thenReturn(0);

        boolean result = ticketManageService.changeReceiveId(new ChangeReceiveIdRequest("requestId", "receiverId"));
        Assertions.assertEquals(true, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme