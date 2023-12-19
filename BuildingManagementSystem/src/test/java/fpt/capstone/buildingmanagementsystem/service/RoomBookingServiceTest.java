package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.mapper.RoomBookingRequestMapper;
import fpt.capstone.buildingmanagementsystem.model.entity.*;
import fpt.capstone.buildingmanagementsystem.model.entity.requestForm.RoomBookingRequestForm;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.RequestStatus;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.RoomBookingStatus;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.TopicEnum;
import fpt.capstone.buildingmanagementsystem.model.request.RoomBookingRequest;
import fpt.capstone.buildingmanagementsystem.model.request.SendRoomBookingRequest;
import fpt.capstone.buildingmanagementsystem.model.response.NotificationAcceptResponse;
import fpt.capstone.buildingmanagementsystem.model.response.RoomBookingResponse;
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

class RoomBookingServiceTest {
    @Mock
    RoomBookingRequestMapper roomBookingRequestMapper;
    @Mock
    RoomRepository roomRepository;
    @Mock
    TicketRepository ticketRepository;
    @Mock
    RequestTicketRepository requestTicketRepository;
    @Mock
    RequestMessageRepository requestMessageRepository;
    @Mock
    DepartmentRepository departmentRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    RoomBookingFormRepository roomBookingFormRepository;
    @Mock
    TicketManageService ticketManageService;
    @Mock
    RoomBookingFormRepositoryV2 roomFormRepositoryV2;
    @Mock
    RoomBookingFormRoomRepository roomBookingRoomRepository;
    @Mock
    RequestOtherService requestOtherService;
    @Mock
    AutomaticNotificationService automaticNotificationService;
    @InjectMocks
    RoomBookingService roomBookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRoomBookingForm() throws ParseException {
        when(roomBookingRequestMapper.convert(any())).thenReturn(new RoomBookingRequestForm("roomBookingRequestId", "title", "content", null, null, null, RoomBookingStatus.PENDING, null, new RequestMessage("requestMessageId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "attachmentMessageId", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, null), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, null), new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), RequestStatus.PENDING, new Ticket("ticketId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), true, TopicEnum.ATTENDANCE_REQUEST), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, null)), null), TopicEnum.ATTENDANCE_REQUEST));
        when(departmentRepository.findByDepartmentId(anyString())).thenReturn(null);
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        when(userRepository.findAllByDepartment(any())).thenReturn(List.of(new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, new Department("departmentId", "departmentName"))));

        boolean result = roomBookingService.getRoomBookingForm(new SendRoomBookingRequest("userId", "ticketId", "requestId", "departmentSenderId", 0, "title", "content", "bookingDate", "startTime", "endTime", "receiverId", "departmentReceiverId"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testGetRoomBookingFormExistTicket() throws ParseException {
        when(roomBookingRequestMapper.convert(any())).thenReturn(new RoomBookingRequestForm("roomBookingRequestId", "title", "content", null, null, null, RoomBookingStatus.PENDING, null, new RequestMessage("requestMessageId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "attachmentMessageId", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, null), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, null), new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), RequestStatus.PENDING, new Ticket("ticketId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), true, TopicEnum.ATTENDANCE_REQUEST), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, null)), null), TopicEnum.ATTENDANCE_REQUEST));
        when(ticketRepository.findByTicketId(anyString())).thenReturn(null);
        when(ticketRepository.updateTicketTime(any(), anyString())).thenReturn(0);
        when(departmentRepository.findByDepartmentId(anyString())).thenReturn(null);
        when(userRepository.findByUserId(anyString())).thenReturn(null);

        boolean result = roomBookingService.getRoomBookingFormExistTicket(new SendRoomBookingRequest("userId", "ticketId", "requestId", "departmentSenderId", 0, "title", "content", "bookingDate", "startTime", "endTime", "receiverId", "departmentReceiverId"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testGetRoomBookingFormExistRequest() throws ParseException {
        when(roomBookingRequestMapper.convert(any())).thenReturn(new RoomBookingRequestForm("roomBookingRequestId", "title", "content", null, null, null, RoomBookingStatus.PENDING, null, new RequestMessage("requestMessageId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "attachmentMessageId", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, null), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, null), new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), RequestStatus.PENDING, new Ticket("ticketId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), true, TopicEnum.ATTENDANCE_REQUEST), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, null)), null), TopicEnum.ATTENDANCE_REQUEST));
        when(ticketRepository.updateTicketTime(any(), anyString())).thenReturn(0);
        when(requestTicketRepository.findByRequestId(anyString())).thenReturn(null);
        when(requestTicketRepository.updateTicketRequestTime(any(), anyString())).thenReturn(0);
        when(requestMessageRepository.findByRequest(any())).thenReturn(List.of(new RequestMessage("requestMessageId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "attachmentMessageId", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, null), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, null), new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), RequestStatus.PENDING, new Ticket("ticketId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), true, TopicEnum.ATTENDANCE_REQUEST), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, null)), new Department("departmentId", "departmentName"))));
        when(departmentRepository.findByDepartmentId(anyString())).thenReturn(null);
        when(userRepository.findByUserId(anyString())).thenReturn(null);

        boolean result = roomBookingService.getRoomBookingFormExistRequest(new SendRoomBookingRequest("userId", "ticketId", "requestId", "departmentSenderId", 0, "title", "content", "bookingDate", "startTime", "endTime", "receiverId", "departmentReceiverId"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testGetPendingAndAcceptedRoom() {
        when(roomFormRepositoryV2.getPendingAndAcceptedRoom()).thenReturn(List.of(new RoomBookingResponse()));

        List<RoomBookingResponse> result = roomBookingService.getPendingAndAcceptedRoom();
        Assertions.assertEquals(List.of(new RoomBookingResponse()), result);
    }

    @Test
    void testAcceptBooking() {
        when(requestTicketRepository.findByTicketRequest(any())).thenReturn(List.of(new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), RequestStatus.PENDING, new Ticket("ticketId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), true, TopicEnum.ATTENDANCE_REQUEST), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, new Department("departmentId", "departmentName")))));
        when(roomBookingFormRepository.findByStartTimeAndEndTime(any(), any())).thenReturn(List.of(new RoomBookingRequestForm("roomBookingRequestId", "title", "content", null, null, null, RoomBookingStatus.PENDING, null, new RequestMessage("requestMessageId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "attachmentMessageId", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, null), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, null), new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), RequestStatus.PENDING, new Ticket("ticketId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), true, TopicEnum.ATTENDANCE_REQUEST), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, null)), null), TopicEnum.ATTENDANCE_REQUEST)));
        when(roomBookingRoomRepository.findByRoomRequestForm(any())).thenReturn(new RoomBookingFormRoom("RoomBookingFormRoomId", new RoomBookingRequestForm("roomBookingRequestId", "title", "content", null, null, null, RoomBookingStatus.PENDING, null, new RequestMessage("requestMessageId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "attachmentMessageId", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, null), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, null), new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), RequestStatus.PENDING, new Ticket("ticketId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), true, TopicEnum.ATTENDANCE_REQUEST), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, null)), null), TopicEnum.ATTENDANCE_REQUEST), new Room(0, "roomName", null)));
        when(roomBookingRoomRepository.findByRoomRequestFormInAndRoom(any(), any())).thenReturn(List.of(new RoomBookingFormRoom("RoomBookingFormRoomId", new RoomBookingRequestForm("roomBookingRequestId", "title", "content", null, null, null, RoomBookingStatus.PENDING, null, new RequestMessage("requestMessageId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "attachmentMessageId", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, null), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, null), new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), RequestStatus.PENDING, new Ticket("ticketId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), true, TopicEnum.ATTENDANCE_REQUEST), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, null)), null), TopicEnum.ATTENDANCE_REQUEST), new Room(0, "roomName", null))));
        when(automaticNotificationService.sendApprovalRequestNotification(any())).thenReturn(new NotificationAcceptResponse("notificationId", "userId", "receiverId", "senderName", true, "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new Department("departmentId", "departmentName")));

        boolean result = roomBookingService.acceptBooking("roomBookingFormRoomId");
        Assertions.assertEquals(true, result);
    }

    @Test
    void testRejectRoomBooking2() {
        when(requestTicketRepository.findByTicketRequest(any())).thenReturn(List.of(new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), RequestStatus.PENDING, new Ticket("ticketId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), true, TopicEnum.ATTENDANCE_REQUEST), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, new Department("departmentId", "departmentName")))));
        when(automaticNotificationService.sendApprovalRequestNotification(any())).thenReturn(new NotificationAcceptResponse("notificationId", "userId", "receiverId", "senderName", true, "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new Department("departmentId", "departmentName")));

        roomBookingService.rejectRoomBooking2(new RoomBookingRequest("roomBookingFormRoomId", "content"), "receiverId");
    }

    @Test
    void testRejectRoomBooking() {
        when(requestTicketRepository.findByTicketRequest(any())).thenReturn(List.of(new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), RequestStatus.PENDING, new Ticket("ticketId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), true, TopicEnum.ATTENDANCE_REQUEST), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), null, new Department("departmentId", "departmentName")))));
        when(automaticNotificationService.sendApprovalRequestNotification(any())).thenReturn(new NotificationAcceptResponse("notificationId", "userId", "receiverId", "senderName", true, "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 53).getTime(), new Department("departmentId", "departmentName")));

        boolean result = roomBookingService.rejectRoomBooking(new RoomBookingRequest("roomBookingFormRoomId", "content"));
        Assertions.assertEquals(true, result);
    }
}
