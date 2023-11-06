package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.mapper.OvertimeRequestFormMapper;
import fpt.capstone.buildingmanagementsystem.model.entity.RequestMessage;
import fpt.capstone.buildingmanagementsystem.model.entity.RequestTicket;
import fpt.capstone.buildingmanagementsystem.model.entity.Ticket;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.entity.requestForm.OvertimeRequestForm;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.RequestStatus;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.TopicEnum;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.TopicOvertime;
import fpt.capstone.buildingmanagementsystem.model.request.SendOvertimeFormRequest;
import fpt.capstone.buildingmanagementsystem.model.response.SystemTimeResponse;
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

class RequestOvertimeFormServiceTest {
    @Mock
    OvertimeRequestFormMapper overtimeRequestFormMapper;
    @Mock
    OvertimeRequestFormRepository overtimeRequestFormRepository;
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
    OvertimeService overtimeService;
    @Mock
    Validate validate;
    @InjectMocks
    RequestOvertimeFormService requestOvertimeFormService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetOvertimeFormUser() throws ParseException {
        when(overtimeRequestFormMapper.convert(any())).thenReturn(new OvertimeRequestForm("overtimeRequestId", null, null, null, TopicOvertime.WEEKEND_AND_NORMAL_DAY, "content", true, new RequestMessage("requestMessageId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), "attachmentMessageId", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), null, null), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), null, null), new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), RequestStatus.PENDING, new Ticket("ticketId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), true, TopicEnum.ATTENDANCE_REQUEST), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), null, null)), null), TopicEnum.ATTENDANCE_REQUEST));
        when(departmentRepository.findByDepartmentId(anyString())).thenReturn(null);
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        when(userRepository.findAllByDepartment(any())).thenReturn(List.of(new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), null, null)));
        when(overtimeService.getSystemTime(anyString(), any())).thenReturn(new SystemTimeResponse(null, null, null));
        when(validate.validateDateFormat(anyString())).thenReturn(true);
        when(validate.checkValidateExistsEvaluate(anyString(), anyString())).thenReturn(true);
        when(validate.validateDateTime(anyString())).thenReturn(true);
        when(validate.validateStartTimeAndEndTime(anyString(), anyString())).thenReturn(true);
        when(validate.validateStartDateAndEndDate2(anyString(), anyString())).thenReturn(true);

        boolean result = requestOvertimeFormService.getOvertimeFormUser(new SendOvertimeFormRequest("userId", "ticketId", "requestId", "overtimeDate", "topicOvertime", "fromTime", "toTime", "title", "content", "departmentId", "receivedId"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testGetOvertimeExistTicket() throws ParseException {
        when(overtimeRequestFormMapper.convert(any())).thenReturn(new OvertimeRequestForm("overtimeRequestId", null, null, null, TopicOvertime.WEEKEND_AND_NORMAL_DAY, "content", true, new RequestMessage("requestMessageId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), "attachmentMessageId", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), null, null), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), null, null), new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), RequestStatus.PENDING, new Ticket("ticketId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), true, TopicEnum.ATTENDANCE_REQUEST), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), null, null)), null), TopicEnum.ATTENDANCE_REQUEST));
        when(ticketRepository.findByTicketId(anyString())).thenReturn(null);
        when(ticketRepository.updateTicketTime(any(), anyString())).thenReturn(0);
        when(departmentRepository.findByDepartmentId(anyString())).thenReturn(null);
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        when(overtimeService.getSystemTime(anyString(), any())).thenReturn(new SystemTimeResponse(null, null, null));
        when(validate.validateDateFormat(anyString())).thenReturn(true);
        when(validate.validateDateTime(anyString())).thenReturn(true);
        when(validate.validateStartTimeAndEndTime(anyString(), anyString())).thenReturn(true);

        boolean result = requestOvertimeFormService.getOvertimeExistTicket(new SendOvertimeFormRequest("userId", "ticketId", "requestId", "overtimeDate", "topicOvertime", "fromTime", "toTime", "title", "content", "departmentId", "receivedId"));
        Assertions.assertEquals(true, result);
    }

    @Test
    void testGetOvertimeExistRequest() throws ParseException {
        when(overtimeRequestFormMapper.convert(any())).thenReturn(new OvertimeRequestForm("overtimeRequestId", null, null, null, TopicOvertime.WEEKEND_AND_NORMAL_DAY, "content", true, new RequestMessage("requestMessageId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), "attachmentMessageId", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), null, null), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), null, null), new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), RequestStatus.PENDING, new Ticket("ticketId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), true, TopicEnum.ATTENDANCE_REQUEST), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), null, null)), null), TopicEnum.ATTENDANCE_REQUEST));
        when(ticketRepository.updateTicketTime(any(), anyString())).thenReturn(0);
        when(requestTicketRepository.findByRequestId(anyString())).thenReturn(null);
        when(requestTicketRepository.updateTicketRequestTime(any(), anyString())).thenReturn(0);
        when(requestMessageRepository.findByRequest(any())).thenReturn(List.of(new RequestMessage("requestMessageId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), "attachmentMessageId", new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), null, null), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), null, null), new RequestTicket("requestId", "title", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), RequestStatus.PENDING, new Ticket("ticketId", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), true, TopicEnum.ATTENDANCE_REQUEST), new User("userId", "firstName", "lastName", "gender", "dateOfBirth", "telephoneNumber", "address", "country", "city", "email", "AcceptedBy", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), "image", new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), new GregorianCalendar(2023, Calendar.DECEMBER, 20, 0, 54).getTime(), null, null)), null)));
        when(departmentRepository.findByDepartmentId(anyString())).thenReturn(null);
        when(userRepository.findByUserId(anyString())).thenReturn(null);
        when(overtimeService.getSystemTime(anyString(), any())).thenReturn(new SystemTimeResponse(null, null, null));
        when(validate.validateDateFormat(anyString())).thenReturn(true);
        when(validate.validateDateTime(anyString())).thenReturn(true);
        when(validate.validateStartTimeAndEndTime(anyString(), anyString())).thenReturn(true);

        boolean result = requestOvertimeFormService.getOvertimeExistRequest(new SendOvertimeFormRequest("userId", "ticketId", "requestId", "overtimeDate", "topicOvertime", "fromTime", "toTime", "title", "content", "departmentId", "receivedId"));
        Assertions.assertEquals(true, result);
    }
}
