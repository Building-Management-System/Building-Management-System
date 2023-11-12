package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.model.entity.*;
import fpt.capstone.buildingmanagementsystem.model.entity.requestForm.OvertimeRequestForm;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.DateType;
import fpt.capstone.buildingmanagementsystem.model.request.ApprovalNotificationRequest;
import fpt.capstone.buildingmanagementsystem.model.request.OvertimeMessageRequest;
import fpt.capstone.buildingmanagementsystem.model.request.SendOtherFormRequest;
import fpt.capstone.buildingmanagementsystem.model.response.OverTimeLogResponse;
import fpt.capstone.buildingmanagementsystem.repository.*;
import fpt.capstone.buildingmanagementsystem.until.Until;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.transaction.Transactional;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static fpt.capstone.buildingmanagementsystem.model.enumEnitty.TopicOvertime.HOLIDAY;
import static fpt.capstone.buildingmanagementsystem.model.enumEnitty.TopicOvertime.WEEKEND_AND_NORMAL_DAY;

@Service
public class OvertimeRequestFormService {
    @Autowired
    AttendanceService attendanceService;
    @Autowired
    TicketRepositoryv2 ticketRepositoryv2;
    @Autowired
    RequestTicketRepository requestTicketRepository;
    @Autowired
    RequestMessageRepository requestMessageRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ControlLogLcdRepository controlLogLcdRepository;
    @Autowired
    OverTimeRepository overTimeRepository;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    OvertimeService overtimeService;
    @Autowired
    RequestOtherService requestOtherService;

    @Autowired
    AutomaticNotificationService automaticNotificationService;

    @Autowired
    private OvertimeRequestFormRepository overtimeRequestFormRepository;

    @Transactional
    public boolean acceptOvertimeRequest(String overtimeRequestId) {
        OvertimeRequestForm overtimeRequest = overtimeRequestFormRepository.findById(overtimeRequestId)
                .orElseThrow(() -> new BadRequest("Not_found_overtime_request"));
        RequestMessage requestMessage = requestMessageRepository.findById(overtimeRequest.getRequestMessage().getRequestMessageId())
                .orElseThrow(() -> new BadRequest("Not_found_request_message"));

        RequestTicket requestTicket = requestTicketRepository.findById(requestMessage.getRequest().getRequestId())
                .orElseThrow(() -> new BadRequest("Not_found_request_ticket"));

        Ticket ticket = ticketRepository.findById(requestTicket.getTicketRequest().getTicketId())
                .orElseThrow(() -> new BadRequest("Not_found_ticket"));

        SendOtherFormRequest sendOtherFormRequest = SendOtherFormRequest.builder()
                .userId(requestMessage.getReceiver().getUserId())
                .ticketId(ticket.getTicketId())
                .requestId(requestTicket.getRequestId())
                .title("Approve attendance request")
                .content("Approve attendance request")
                .departmentId(requestMessage.getDepartment().getDepartmentId())
                .receivedId(requestMessage.getSender().getUserId())
                .build();
        List<RequestTicket> requestTickets = requestTicketRepository.findByTicketRequest(ticket);

        executeRequestDecision(requestTickets, ticket, sendOtherFormRequest);
        try {
            overtimeRequest.setStatus(true);
            overtimeRequestFormRepository.save(overtimeRequest);
            requestMessageRepository.saveAndFlush(requestMessage);
            requestTicketRepository.saveAll(requestTickets);
            ticketRepository.save(ticket);
            saveOverTimeLog(overtimeRequest, requestMessage);
            automaticNotificationService.sendApprovalRequestNotification(
                    new ApprovalNotificationRequest(
                            ticket.getTicketId(),
                            requestMessage.getReceiver(),
                            requestMessage.getSender(),
                            ticket.getTopic(),
                            true,
                            null
                    ));
            return true;
        } catch (Exception e) {
            throw new ServerError("Fail");
        }
    }

    private void saveOverTimeLog(OvertimeRequestForm overtimeRequest, RequestMessage requestMessage) {
        String username= requestMessage.getSender().getAccount().getUsername();
        Date date= overtimeRequest.getOvertimeDate();
        List<ControlLogLcd> list= controlLogLcdRepository.getControlLogLcdList(username,date.toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        //endTime
        LocalDateTime localDateTime1 = LocalDateTime.parse(list.get(0).getTime().toString().replace(".0",""), formatter);
        Time sqlTimeEnd = Time.valueOf(localDateTime1.toLocalTime());
        //date Ot
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        //approvalTime Ot
        java.sql.Date sqlDate2 = new java.sql.Date(Until.generateDate().getTime());

        double totalPaid=0.0;
        double time= 0.0;
        String startTime = null;
        if((attendanceService.getCheckWeekend(date) == Calendar.SATURDAY) ||
                (attendanceService.getCheckWeekend(date) == Calendar.SUNDAY) ||
                (overtimeRequest.getTopicOvertime()==HOLIDAY)) {
            //totalTimeWeekEndAndHolidayDay Ot and startTimeTimeWeekEndAndHoliday Ot
            time= overtimeService.getTime(overtimeRequest.getFromTime(), overtimeRequest.getToTime());
            String checkMorningTime="1999-01-01 12:00:00";
            String checkAfternoonTime="1999-01-01 13:00:00";
            LocalDateTime localCheckMorningTime = LocalDateTime.parse(checkMorningTime, formatter);
            Time checkTimeStart = Time.valueOf(localCheckMorningTime.toLocalTime());
            LocalDateTime localCheckAfternoonTime = LocalDateTime.parse(checkAfternoonTime, formatter);
            Time checkTimeEnd = Time.valueOf(localCheckAfternoonTime.toLocalTime());
            System.out.println(overtimeRequest.getFromTime().getTime());
            System.out.println(checkTimeStart.getTime());

            if(overtimeRequest.getFromTime().getTime()-checkTimeStart.getTime()<0
                    &&checkTimeEnd.getTime()-overtimeRequest.getToTime().getTime()<0){
                time=time-1.0;
            }
            startTime="1999-01-01 "+ overtimeRequest.getFromTime();
        }else {
            //totalTimeNormalDay Ot and startTimeNormalDay Ot
            time= overtimeService.getTime(overtimeRequest.getFromTime(), overtimeRequest.getToTime());
            startTime="1999-01-01 18:00:00";
        }
        LocalDateTime localDateTime3 = LocalDateTime.parse(startTime, formatter);
        Time sqlTimeStart = Time.valueOf(localDateTime3.toLocalTime());

        //Rounding working time
        int convertTime = (int) time;
        double timeToCheck2 = convertTime + 0.5;
        double timeToCheck3 = convertTime + 1;
        if((double) convertTime == time){
            time= convertTime;
        }
        if((double) convertTime < time && time <= timeToCheck2){
            time=timeToCheck2;
        }
        if(timeToCheck2 < time && time <= timeToCheck3){
            time=timeToCheck3;
        }
        //Work calculation
        if(overtimeRequest.getTopicOvertime()==WEEKEND_AND_NORMAL_DAY){
            totalPaid=time*2;
        }
        if(overtimeRequest.getTopicOvertime()==HOLIDAY){
            totalPaid=time*3;
        }
        Optional<OvertimeLog> overtimeLogCheck=overTimeRepository.findByUser_Account_UsernameAndDate(username,sqlDate);
        OvertimeLog overtimeLog;
        //check update or save
        if(overtimeLogCheck.isPresent()){
            overtimeLog = OvertimeLog.builder().otId(overtimeLogCheck.get().getOtId()).date(sqlDate).manualEnd(overtimeRequest.getToTime()).
                    manualStart(overtimeRequest.getFromTime()).startTime(sqlTimeStart).endTime(sqlTimeEnd).dateType(overtimeRequest.getTopicOvertime()).
                    approvedDate(sqlDate2).totalPaid(totalPaid).user(requestMessage.getSender()).
                    build();
        }else {
            overtimeLog = OvertimeLog.builder().date(sqlDate).manualEnd(overtimeRequest.getToTime()).
                    manualStart(overtimeRequest.getFromTime()).startTime(sqlTimeStart).endTime(sqlTimeEnd).dateType(overtimeRequest.getTopicOvertime()).
                    approvedDate(sqlDate2).totalPaid(totalPaid).user(requestMessage.getSender()).
                    build();
        }
        overTimeRepository.save(overtimeLog);
    }

    @Transactional
    public boolean rejectOvertimeRequest(OvertimeMessageRequest overtimeMessageRequest) {
        OvertimeRequestForm overtimeRequest = overtimeRequestFormRepository.findById(overtimeMessageRequest.getOverTimeRequestId())
                .orElseThrow(() -> new BadRequest("Not_found_overtime_request"));
        RequestMessage requestMessage = requestMessageRepository.findById(overtimeRequest.getRequestMessage().getRequestMessageId())
                .orElseThrow(() -> new BadRequest("Not_found_request_message"));

        RequestTicket requestTicket = requestTicketRepository.findById(requestMessage.getRequest().getRequestId())
                .orElseThrow(() -> new BadRequest("Not_found_request_ticket"));

        Ticket ticket = ticketRepository.findById(requestTicket.getTicketRequest().getTicketId())
                .orElseThrow(() -> new BadRequest("Not_found_ticket"));

        SendOtherFormRequest sendOtherFormRequest = SendOtherFormRequest.builder()
                .userId(requestMessage.getReceiver().getUserId())
                .ticketId(ticket.getTicketId())
                .requestId(requestTicket.getRequestId())
                .title("Reject Attendance request")
                .content(overtimeMessageRequest.getContent())
                .departmentId(requestMessage.getDepartment().getDepartmentId())
                .receivedId(requestMessage.getSender().getUserId())
                .build();
        List<RequestTicket> requestTickets = requestTicketRepository.findByTicketRequest(ticket);
        executeRequestDecision(requestTickets, ticket, sendOtherFormRequest);

        try {
            requestMessageRepository.saveAndFlush(requestMessage);
            requestTicketRepository.saveAll(requestTickets);
            ticketRepository.save(ticket);
            automaticNotificationService.sendApprovalRequestNotification(
                    new ApprovalNotificationRequest(
                            ticket.getTicketId(),
                            requestMessage.getReceiver(),
                            requestMessage.getSender(),
                            ticket.getTopic(),
                            false,
                            overtimeMessageRequest.getContent()
                    ));
            return true;
        } catch (Exception e) {
            throw new ServerError("Fail");
        }
    }

    private void executeRequestDecision(List<RequestTicket> requestTickets, Ticket ticket, SendOtherFormRequest sendOtherFormRequest) {
        RequestAttendanceFromService.executeDuplicate(requestTickets, ticket, sendOtherFormRequest, requestOtherService, requestTicketRepository);
    }

}
