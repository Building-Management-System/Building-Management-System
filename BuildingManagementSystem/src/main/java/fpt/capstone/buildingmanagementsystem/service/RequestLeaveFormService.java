package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.mapper.LeaveRequestFormMapper;
import fpt.capstone.buildingmanagementsystem.model.entity.*;
import fpt.capstone.buildingmanagementsystem.model.entity.requestForm.LeaveRequestForm;
import fpt.capstone.buildingmanagementsystem.model.request.SendLeaveFormRequest;
import fpt.capstone.buildingmanagementsystem.repository.*;
import fpt.capstone.buildingmanagementsystem.until.Until;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Optional;

import static fpt.capstone.buildingmanagementsystem.model.enumEnitty.RequestStatus.PENDING;
import static fpt.capstone.buildingmanagementsystem.model.enumEnitty.TopicEnum.LEAVE_REQUEST;
import static fpt.capstone.buildingmanagementsystem.validate.Validate.*;

@Service
public class RequestLeaveFormService {
    @Autowired
    LeaveRequestFormRepository leaveRequestFormRepository;
    @Autowired
    LeaveRequestFormMapper leaveRequestFormMapper;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    RequestTicketRepository requestTicketRepository;
    @Autowired
    RequestMessageRepository requestMessageRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    UserRepository userRepository;

    public boolean getLeaveFormUser(SendLeaveFormRequest sendLeaveFormRequest) {
        try {
            if (sendLeaveFormRequest.getContent() != null &&
                    sendLeaveFormRequest.getDepartmentId() != null &&
                    sendLeaveFormRequest.getTitle() != null &&
                    sendLeaveFormRequest.getFromDate() != null &&
                    sendLeaveFormRequest.getToDate() != null
            ) {
                if (checkValidate(sendLeaveFormRequest)) {
                    Optional<User> send_user = userRepository.findByUserId(sendLeaveFormRequest.getUserId());
                    Optional<Department> department = departmentRepository.findByDepartmentId(sendLeaveFormRequest.getDepartmentId());
                    if (send_user.isPresent() && department.isPresent()) {
                        String id_ticket = "LV_" + Until.generateId();
                        String id_request_ticket = "LV_" + Until.generateId();
                        Ticket ticket = Ticket.builder()
                                .ticketId(id_ticket)
                                .topic(LEAVE_REQUEST)
                                .status(false)
                                .createDate(Until.generateRealTime())
                                .updateDate(Until.generateRealTime())
                                .build();
                        ticketRepository.save(ticket);
                        saveLeaveRequest(sendLeaveFormRequest, send_user, department, id_request_ticket, ticket);
                        return true;
                    } else {
                        throw new NotFound("not_found");
                    }
                } else {
                    throw new BadRequest("date_time_input_wrong");
                }
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (ServerError | ParseException e) {
            throw new ServerError("fail");
        }
    }

    public boolean getLeaveFormUserExistTicket(SendLeaveFormRequest sendLeaveFormRequest) {
        try {
            if (sendLeaveFormRequest.getContent() != null &&
                    sendLeaveFormRequest.getDepartmentId() != null &&
                    sendLeaveFormRequest.getTitle() != null &&
                    sendLeaveFormRequest.getFromDate() != null &&
                    sendLeaveFormRequest.getToDate() != null &&
                    sendLeaveFormRequest.getTicketId() != null
            ) {
                if (checkValidate(sendLeaveFormRequest)) {
                    Optional<User> send_user = userRepository.findByUserId(sendLeaveFormRequest.getUserId());
                    Optional<Department> department = departmentRepository.findByDepartmentId(sendLeaveFormRequest.getDepartmentId());
                    Optional<Ticket> ticket = ticketRepository.findByTicketId(sendLeaveFormRequest.getTicketId());
                    if (send_user.isPresent() && department.isPresent() && ticket.isPresent()) {
                        String id_request_ticket = "LV_" + Until.generateId();
                        saveLeaveRequest(sendLeaveFormRequest, send_user, department, id_request_ticket, ticket.get());
                        ticketRepository.updateTicketTime(Until.generateRealTime(), sendLeaveFormRequest.getTicketId());
                        return true;
                    } else {
                        throw new NotFound("not_found");
                    }
                } else {
                    throw new BadRequest("date_time_input_wrong");
                }
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (ServerError e) {
            throw new ServerError("fail");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean getLeaveFormUserExistRequest(SendLeaveFormRequest sendLeaveFormRequest) {
        try {
            if (sendLeaveFormRequest.getContent() != null &&
                    sendLeaveFormRequest.getDepartmentId() != null &&
                    sendLeaveFormRequest.getFromDate() != null &&
                    sendLeaveFormRequest.getToDate() != null &&
                    sendLeaveFormRequest.getRequestId() != null
            ) {
                if (checkValidate(sendLeaveFormRequest)) {
                    Optional<User> send_user = userRepository.findByUserId(sendLeaveFormRequest.getUserId());
                    Optional<Department> department = departmentRepository.findByDepartmentId(sendLeaveFormRequest.getDepartmentId());
                    Optional<RequestTicket> requestTicket = requestTicketRepository.findByRequestId(sendLeaveFormRequest.getRequestId());
                    if (send_user.isPresent() && department.isPresent() && requestTicket.isPresent()) {
                        saveLeaveMessage(sendLeaveFormRequest, send_user, department, requestTicket.get());
                        ticketRepository.updateTicketTime(Until.generateRealTime(),requestTicket.get().getTicketRequest().getTicketId());
                        requestTicketRepository.updateTicketRequestTime(Until.generateRealTime(),sendLeaveFormRequest.getRequestId());                        return true;
                    } else {
                        throw new NotFound("not_found");
                    }
                } else {
                    throw new BadRequest("date_time_input_wrong");
                }
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (ServerError e) {
            throw new ServerError("fail");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean checkValidate(SendLeaveFormRequest sendLeaveFormRequest) throws ParseException {
        return validateDateFormat(sendLeaveFormRequest.getFromDate()) &&
                validateDateFormat(sendLeaveFormRequest.getToDate()) && validateStartDateAndEndDate(sendLeaveFormRequest.getFromDate()
                , sendLeaveFormRequest.getToDate());
    }
    private void saveLeaveRequest(SendLeaveFormRequest sendLeaveFormRequest, Optional<User> send_user, Optional<Department> department, String id_request_ticket, Ticket ticket) {
        RequestTicket requestTicket = RequestTicket.builder()
                .requestId(id_request_ticket)
                .createDate(Until.generateRealTime().toString())
                .updateDate(Until.generateRealTime().toString())
                .status(PENDING)
                .ticketRequest(ticket)
                .title(sendLeaveFormRequest.getTitle())
                .user(send_user.get()).build();
        saveLeaveMessage(sendLeaveFormRequest, send_user, department, requestTicket);
    }

    private void saveLeaveMessage(SendLeaveFormRequest sendLeaveFormRequest, Optional<User> send_user, Optional<Department> department, RequestTicket requestTicket) {
        RequestMessage requestMessage = RequestMessage.builder()
                .createDate(Until.generateRealTime())
                .updateDate(Until.generateRealTime())
                .sender(send_user.get())
                .request(requestTicket)
                .department(department.get())
                .build();
        if (sendLeaveFormRequest.getReceivedId() != null) {
            Optional<User> receive_user = userRepository.findByUserId(sendLeaveFormRequest.getReceivedId());
            requestMessage.setReceiver(receive_user.get());
        }
        LeaveRequestForm leaveRequestForm = leaveRequestFormMapper.convert(sendLeaveFormRequest);
        leaveRequestForm.setTopic(LEAVE_REQUEST);
        leaveRequestForm.setStatus(false);
        leaveRequestForm.setRequestMessage(requestMessage);
        requestTicketRepository.save(requestTicket);
        requestMessageRepository.save(requestMessage);
        leaveRequestFormRepository.save(leaveRequestForm);
    }
}
