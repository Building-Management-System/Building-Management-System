package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.mapper.WorkingOutsideMapper;
import fpt.capstone.buildingmanagementsystem.model.entity.RequestMessage;
import fpt.capstone.buildingmanagementsystem.model.entity.RequestTicket;
import fpt.capstone.buildingmanagementsystem.model.entity.Ticket;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.entity.requestForm.WorkingOutsideRequestForm;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.RequestStatus;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.TopicEnum;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.WorkingOutsideType;
import fpt.capstone.buildingmanagementsystem.model.request.SendWorkingOutSideRequest;
import fpt.capstone.buildingmanagementsystem.repository.*;
import fpt.capstone.buildingmanagementsystem.validate.Validate;
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

class RequestOutSideWorkFromServiceTest {
    @Mock
    WorkingOutsideMapper workingOutsideMapper;
    @Mock
    WorkingOutsideFormRepository workingOutsideFormRepository;
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
    AutomaticNotificationService automaticNotificationService;
    @Mock
    Validate validate;
    @InjectMocks
    RequestOutSideWorkFromService requestOutSideWorkFromService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOutSideFormUser() throws ParseException {
        when(workingOutsideMapper.convert(any())).thenReturn(new WorkingOutsideRequestForm("workingOutsideId", null, WorkingOutsideType.HALF_MORNING, "content", true, new RequestMessage("requestMessageId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), "attachmentMessageId", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), null, null), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), null, null), new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), RequestStatus.PENDING, new Ticket("ticketId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), true, TopicEnum.ATTENDANCE_REQUEST), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), null, null)), null), TopicEnum.ATTENDANCE_REQUEST));
        when(departmentRepository.findByDepartmentId(anyString())).thenReturn(null);
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        when(userRepository.findAllByDepartment(any())).thenReturn(List.of(new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), null, null)));
        when(validate.validateDateFormat(anyString())).thenReturn(true);
        when(validate.checkValidateExistsEvaluate(anyString(), anyString())).thenReturn(true);

        boolean result = requestOutSideWorkFromService.getOutSideFormUser(new SendWorkingOutSideRequest("userId", "ticketId", "requestId", "date", "topic", "type", "title", "content", "departmentId", "receivedId"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testGetOutSideFormUserExistTicket() throws ParseException {
        when(workingOutsideMapper.convert(any())).thenReturn(new WorkingOutsideRequestForm("workingOutsideId", null, WorkingOutsideType.HALF_MORNING, "content", true, new RequestMessage("requestMessageId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), "attachmentMessageId", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), null, null), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), null, null), new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), RequestStatus.PENDING, new Ticket("ticketId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), true, TopicEnum.ATTENDANCE_REQUEST), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), null, null)), null), TopicEnum.ATTENDANCE_REQUEST));
        when(ticketRepository.findByTicketId(anyString())).thenReturn(null);
        when(ticketRepository.updateTicketTime(any(), anyString())).thenReturn(0);
        when(departmentRepository.findByDepartmentId(anyString())).thenReturn(null);
        when(userRepository.findByUserId(anyString())).thenReturn(null);

        boolean result = requestOutSideWorkFromService.getOutSideFormUserExistTicket(new SendWorkingOutSideRequest("userId", "ticketId", "requestId", "date", "topic", "type", "title", "content", "departmentId", "receivedId"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testGetOutSideFormUserExistRequest() throws ParseException {
        when(workingOutsideMapper.convert(any())).thenReturn(new WorkingOutsideRequestForm("workingOutsideId", null, WorkingOutsideType.HALF_MORNING, "content", true, new RequestMessage("requestMessageId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), "attachmentMessageId", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), null, null), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), null, null), new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), RequestStatus.PENDING, new Ticket("ticketId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), true, TopicEnum.ATTENDANCE_REQUEST), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), null, null)), null), TopicEnum.ATTENDANCE_REQUEST));
        when(ticketRepository.updateTicketTime(any(), anyString())).thenReturn(0);
        when(requestTicketRepository.findByRequestId(anyString())).thenReturn(null);
        when(requestTicketRepository.updateTicketRequestTime(any(), anyString())).thenReturn(0);
        when(requestMessageRepository.findByRequest(any())).thenReturn(List.of(new RequestMessage("requestMessageId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), "attachmentMessageId", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), null, null), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), null, null), new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), RequestStatus.PENDING, new Ticket("ticketId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), true, TopicEnum.ATTENDANCE_REQUEST), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 55).getTime(), null, null)), null)));
        when(departmentRepository.findByDepartmentId(anyString())).thenReturn(null);
        when(userRepository.findByUserId(anyString())).thenReturn(null);

        boolean result = requestOutSideWorkFromService.getOutSideFormUserExistRequest(new SendWorkingOutSideRequest("userId", "ticketId", "requestId", "date", "topic", "type", "title", "content", "departmentId", "receivedId"));
        Assertions.assertEquals(true, result);
    }
}
