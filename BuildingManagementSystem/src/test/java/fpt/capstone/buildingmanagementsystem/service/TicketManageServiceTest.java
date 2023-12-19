package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.model.dto.TicketDto;
import fpt.capstone.buildingmanagementsystem.model.dto.TicketRequestDto;
import fpt.capstone.buildingmanagementsystem.model.entity.*;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.RequestStatus;
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
    //pass2

    @Test
    void testGetAllTicketsBySenderId() {
        when(ticketRepositoryv2.getTicketRequestBySenderId(anyString())).thenReturn(List.of(new TicketRequestDto()));

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
        when(ticketRepositoryv2.getTicketRequestByDepartment(anyString())).thenReturn(List.of(new TicketRequestDto()));

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

    @Test
    void testUpdateTicketOfNewManager() {
        when(ticketRepositoryv2.getTicketRequestByDepartment(anyString())).thenReturn(List.of(new TicketRequestDto()));

        ticketManageService.updateTicketOfNewManager(null);
    }

    @Test
    void testUpdateTicketOfNewManager2() {
        when(ticketRepositoryv2.getTicketRequestBySenderId(anyString())).thenReturn(List.of(new TicketRequestDto()));
        when(ticketRepositoryv2.getTicketRequestByDepartment(anyString())).thenReturn(List.of(new TicketRequestDto()));
        when(requestTicketRepository.findByRequestIdIn(any())).thenReturn(List.of(new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), RequestStatus.PENDING, null, new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), null, null))));

        ticketManageService.updateTicketOfNewManager(null, new InactiveManagerTemp("tempId", null, null));
    }

    @Test
    void testResetTicketData() {
        when(requestMessageRepository.findAllByReceiver(any())).thenReturn(List.of(new RequestMessage("requestMessageId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), "attachmentMessageId", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), null), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), null), new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), RequestStatus.PENDING, null, new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), null, null)), new Department("departmentId", "departmentName"))));
        when(requestMessageRepository.findAllBySender(any())).thenReturn(List.of(new RequestMessage("requestMessageId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), "attachmentMessageId", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), null), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), null), new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), RequestStatus.PENDING, null, new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), null, null)), new Department("departmentId", "departmentName"))));

        ticketManageService.resetTicketData(new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new Account("accountId", "username", "password", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), "createdBy", null, new Status("statusId", "statusName"), new Role("roleId", "roleName")), null));
    }

    @Test
    void testCloseAllTicketWhenAcceptEvaluate() {
        when(ticketRepository.findByUserIdAndMonthAndYear(anyString(), anyInt(), anyInt())).thenReturn(List.of(null));
        when(requestTicketRepository.findByTicketRequestIn(any())).thenReturn(List.of(new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), RequestStatus.PENDING, null, new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 49).getTime(), null, null))));

        ticketManageService.closeAllTicketWhenAcceptEvaluate("employeeId", 0, 0);
    }
}
