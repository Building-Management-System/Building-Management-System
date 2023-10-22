package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.mapper.AttendanceRequestFormMapper;
import fpt.capstone.buildingmanagementsystem.model.entity.*;
import fpt.capstone.buildingmanagementsystem.model.entity.requestForm.AttendanceRequestForm;
import fpt.capstone.buildingmanagementsystem.model.request.SendAttendanceFormRequest;
import fpt.capstone.buildingmanagementsystem.repository.*;
import fpt.capstone.buildingmanagementsystem.until.Until;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Optional;

import static fpt.capstone.buildingmanagementsystem.model.enumEnitty.RequestStatus.PENDING;
import static fpt.capstone.buildingmanagementsystem.model.enumEnitty.TopicEnum.ATTENDANCE_REQUEST;
import static fpt.capstone.buildingmanagementsystem.validate.Validate.*;

@Service
public class RequestAttendanceFromService {
    @Autowired
    AttendanceRequestFormMapper attendanceRequestFormMapper;
    @Autowired
    AttendanceRequestFormRepository attendanceRequestFormRepository;
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
    TicketRepository ticketRepository;

    public boolean getAttendanceUser(SendAttendanceFormRequest sendAttendanceFormRequest) {
        try {
            if (sendAttendanceFormRequest.getContent() != null &&
                    sendAttendanceFormRequest.getDepartmentId() != null &&
                    sendAttendanceFormRequest.getManualDate() != null &&
                    sendAttendanceFormRequest.getTitle() != null &&
                    sendAttendanceFormRequest.getManualLastExit() != null
            ) {
                if (checkValidate(sendAttendanceFormRequest)) {
                    Optional<User> send_user = userRepository.findByUserId(sendAttendanceFormRequest.getUserId());
                    Optional<Department> department = departmentRepository.findByDepartmentId(sendAttendanceFormRequest.getDepartmentId());
                    if (send_user.isPresent() && department.isPresent()) {
                        String id_ticket = "AT_" + Until.generateId();
                        String id_request_ticket = "AT_" + Until.generateId();
                        Ticket ticket = Ticket.builder().ticketId(id_ticket).topic(ATTENDANCE_REQUEST).status(false).createDate(Until.generateRealTime())
                                .updateDate(Until.generateRealTime()).build();
                        ticketRepository.save(ticket);
                        saveAttendanceRequest(sendAttendanceFormRequest, send_user, department, id_request_ticket, ticket);
                        return true;
                    } else {
                        throw new NotFound("not_found");
                    }
                }else {
                    throw new BadRequest("date_time_input_wrong");
                }
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (ServerError | ParseException e) {
            throw new ServerError("fail");
        }
    }

    public boolean getAttendanceUserExistTicket(SendAttendanceFormRequest sendAttendanceFormRequest) {
        try {
            if (sendAttendanceFormRequest.getContent() != null &&
                    sendAttendanceFormRequest.getDepartmentId() != null &&
                    sendAttendanceFormRequest.getManualDate() != null &&
                    sendAttendanceFormRequest.getTitle() != null &&
                    sendAttendanceFormRequest.getManualLastExit() != null &&
                    sendAttendanceFormRequest.getTicketId() != null
            ) {
                if (checkValidate(sendAttendanceFormRequest)) {
                    Optional<User> send_user = userRepository.findByUserId(sendAttendanceFormRequest.getUserId());
                    Optional<Department> department = departmentRepository.findByDepartmentId(sendAttendanceFormRequest.getDepartmentId());
                    Optional<Ticket> ticket = ticketRepository.findByTicketId(sendAttendanceFormRequest.getTicketId());
                    if (send_user.isPresent() && department.isPresent() && ticket.isPresent()) {
                        String id_request_ticket = "AT_" + Until.generateId();
                        saveAttendanceRequest(sendAttendanceFormRequest, send_user, department, id_request_ticket, ticket.get());
                        ticketRepository.updateTicketTime(Until.generateRealTime(),sendAttendanceFormRequest.getTicketId());
                        return true;
                    } else {
                        throw new NotFound("not_found");
                    }
                }else {
                    throw new BadRequest("date_time_input_wrong");
                }
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (ServerError | ParseException e) {
            throw new ServerError("fail");
        }
    }
    public boolean getAttendanceUserExistRequest(SendAttendanceFormRequest sendAttendanceFormRequest) {
        try {
            if (sendAttendanceFormRequest.getContent() != null &&
                    sendAttendanceFormRequest.getDepartmentId() != null &&
                    sendAttendanceFormRequest.getManualDate() != null &&
                    sendAttendanceFormRequest.getManualLastExit() != null &&
                    sendAttendanceFormRequest.getRequestId() != null
            ) {
                if (checkValidate(sendAttendanceFormRequest)) {
                    Optional<User> send_user = userRepository.findByUserId(sendAttendanceFormRequest.getUserId());
                    Optional<Department> department = departmentRepository.findByDepartmentId(sendAttendanceFormRequest.getDepartmentId());
                    Optional<RequestTicket> request = requestTicketRepository.findByRequestId(sendAttendanceFormRequest.getRequestId());
                    if (send_user.isPresent() && department.isPresent() && request.isPresent()) {
                        saveAttendanceMessage(sendAttendanceFormRequest, send_user, department, request.get());
                        ticketRepository.updateTicketTime(Until.generateRealTime(),request.get().getTicketRequest().getTicketId());
                        requestTicketRepository.updateTicketRequestTime(Until.generateRealTime(),sendAttendanceFormRequest.getRequestId());
                        return true;
                    } else {
                        throw new NotFound("not_found");
                    }
                }else {
                    throw new BadRequest("date_time_input_wrong");
                }
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (ServerError | ParseException e) {
            throw new ServerError("fail");
        }
    }
    private static boolean checkValidate(SendAttendanceFormRequest sendAttendanceFormRequest) throws ParseException {
        return validateDateFormat(sendAttendanceFormRequest.getManualDate()) &&
                validateDateTime(sendAttendanceFormRequest.getManualFirstEntry()) &&
                validateDateTime(sendAttendanceFormRequest.getManualLastExit()) &&
                validateStartTimeAndEndTime(sendAttendanceFormRequest.getManualFirstEntry(), sendAttendanceFormRequest.getManualLastExit());
    }

    private void saveAttendanceRequest(SendAttendanceFormRequest sendAttendanceFormRequest, Optional<User> send_user, Optional<Department> department, String id_request_ticket, Ticket ticket) {
        RequestTicket requestTicket = RequestTicket.builder().requestId(id_request_ticket).createDate(Until.generateRealTime())
                .updateDate(Until.generateRealTime())
                .status(PENDING).ticketRequest(ticket).title(sendAttendanceFormRequest.getTitle()).user(send_user.get()).build();
        saveAttendanceMessage(sendAttendanceFormRequest, send_user, department, requestTicket);
    }

    private void saveAttendanceMessage(SendAttendanceFormRequest sendAttendanceFormRequest, Optional<User> send_user, Optional<Department> department, RequestTicket requestTicket) {
        RequestMessage requestMessage = RequestMessage.builder().createDate(Until.generateRealTime())
                .updateDate(Until.generateRealTime())
                .sender(send_user.get()).request(requestTicket).department(department.get()).build();
        if (sendAttendanceFormRequest.getReceivedId() != null) {
            Optional<User> receive_user = userRepository.findByUserId(sendAttendanceFormRequest.getReceivedId());
            requestMessage.setReceiver(receive_user.get());
        }
        AttendanceRequestForm attendanceRequestForm = attendanceRequestFormMapper.convert(sendAttendanceFormRequest);
        attendanceRequestForm.setTopic(ATTENDANCE_REQUEST);
        attendanceRequestForm.setStatus(false);
        attendanceRequestForm.setRequestMessage(requestMessage);
        requestTicketRepository.save(requestTicket);
        requestMessageRepository.save(requestMessage);
        attendanceRequestFormRepository.save(attendanceRequestForm);
    }

}
