package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.mapper.AttendanceRequestFormMapper;
import fpt.capstone.buildingmanagementsystem.model.entity.Department;
import fpt.capstone.buildingmanagementsystem.model.entity.RequestMessage;
import fpt.capstone.buildingmanagementsystem.model.entity.RequestTicket;
import fpt.capstone.buildingmanagementsystem.model.entity.Ticket;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.entity.requestForm.AttendanceRequestForm;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.RequestStatus;
import fpt.capstone.buildingmanagementsystem.model.request.SendAttendanceFormRequest;
import fpt.capstone.buildingmanagementsystem.repository.AttendanceRequestFormRepository;
import fpt.capstone.buildingmanagementsystem.repository.DepartmentRepository;
import fpt.capstone.buildingmanagementsystem.repository.RequestMessageRepository;
import fpt.capstone.buildingmanagementsystem.repository.RequestTicketRepository;
import fpt.capstone.buildingmanagementsystem.repository.TicketRepository;
import fpt.capstone.buildingmanagementsystem.repository.TicketRepositoryv2;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import fpt.capstone.buildingmanagementsystem.until.Until;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static fpt.capstone.buildingmanagementsystem.model.enumEnitty.RequestStatus.ANSWERED;
import static fpt.capstone.buildingmanagementsystem.model.enumEnitty.RequestStatus.PENDING;
import static fpt.capstone.buildingmanagementsystem.model.enumEnitty.TopicEnum.ATTENDANCE_REQUEST;
import static fpt.capstone.buildingmanagementsystem.validate.Validate.validateDateFormat;
import static fpt.capstone.buildingmanagementsystem.validate.Validate.validateDateTime;
import static fpt.capstone.buildingmanagementsystem.validate.Validate.validateStartTimeAndEndTime;

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
                        ticketRepository.updateTicketTime(Until.generateRealTime(), sendAttendanceFormRequest.getTicketId());
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
                        List<RequestMessage> requestMessageOptional = requestMessageRepository.findByRequest(request.get());
                        String senderId = requestMessageOptional.get(0).getSender().getUserId();
                        if (requestMessageOptional.get(0).getReceiver() != null) {
                            String receiverId = requestMessageOptional.get(0).getReceiver().getUserId();
                            if (Objects.equals(sendAttendanceFormRequest.getUserId(), senderId)) {
                                sendAttendanceFormRequest.setReceivedId(receiverId);
                            } else {
                                sendAttendanceFormRequest.setReceivedId(senderId);
                            }
                        }
                        RequestStatus status = request.get().getStatus();
                        if (!status.equals(ANSWERED) && !Objects.equals(senderId, sendAttendanceFormRequest.getUserId())) {
                            request.get().setStatus(ANSWERED);
                            requestTicketRepository.save(request.get());
                        }
                        saveAttendanceMessage(sendAttendanceFormRequest, send_user, department, request.get());
                        String time = Until.generateRealTime();
                        ticketRepository.updateTicketTime(time, request.get().getTicketRequest().getTicketId());
                        requestTicketRepository.updateTicketRequestTime(time, sendAttendanceFormRequest.getRequestId());
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

    @Transactional
    public boolean acceptAttendanceRequest(String attendanceRequestId) {
        AttendanceRequestForm attendanceRequestForm = attendanceRequestFormRepository.findById(attendanceRequestId)
                .orElseThrow(() -> new BadRequest("Not_found_attendance_id"));

        RequestMessage requestMessage = requestMessageRepository.findById(attendanceRequestForm.getRequestMessage().getRequestMessageId())
                .orElseThrow(() -> new BadRequest("Not_found_request_message"));

        RequestTicket requestTicket = requestTicketRepository.findById(requestMessage.getRequest().getRequestId())
                .orElseThrow(() -> new BadRequest("Not_found_request_ticket"));

        Ticket ticket = ticketRepository.findById(requestTicket.getTicketRequest().getTicketId())
                .orElseThrow(() -> new BadRequest("Not_found_ticket"));

        ticket.setUpdateDate(Until.generateRealTime());
        ticket.setStatus(true);
        requestTicket.setUpdateDate(Until.generateRealTime());
        requestTicket.setStatus(RequestStatus.CLOSED);
        requestMessage.setUpdateDate(Until.generateRealTime());

        try {
            attendanceRequestForm.setStatus(true);
            attendanceRequestFormRepository.save(attendanceRequestForm);
            requestMessageRepository.saveAndFlush(requestMessage);
            requestTicketRepository.saveAndFlush(requestTicket);
            ticketRepository.save(ticket);
            return true;
        } catch (Exception e) {
            throw new ServerError("Fail");
        }
    }

    @Transactional
    public boolean rejectAttendanceRequest(String attendanceRequestFormId) {
        AttendanceRequestForm roomBookingRequestForm = attendanceRequestFormRepository.findById(attendanceRequestFormId)
                .orElseThrow(() -> new BadRequest("Not_found_form"));

        RequestMessage requestMessage = requestMessageRepository.findById(roomBookingRequestForm.getRequestMessage().getRequestMessageId())
                .orElseThrow(() -> new BadRequest("Not_found_request_message"));

        RequestTicket requestTicket = requestTicketRepository.findById(requestMessage.getRequest().getRequestId())
                .orElseThrow(() -> new BadRequest("Not_found_request_ticket"));

        Ticket ticket = ticketRepository.findById(requestTicket.getTicketRequest().getTicketId())
                .orElseThrow(() -> new BadRequest("Not_found_ticket"));
        ticket.setUpdateDate(Until.generateRealTime());
        ticket.setStatus(true);
        requestTicket.setUpdateDate(Until.generateRealTime());
        requestTicket.setStatus(RequestStatus.CLOSED);
        requestMessage.setUpdateDate(Until.generateRealTime());
        try {
            requestMessageRepository.saveAndFlush(requestMessage);
            requestTicketRepository.saveAndFlush(requestTicket);
            ticketRepository.save(ticket);
            return true;
        } catch (Exception e) {
            throw new ServerError("Fail");
        }
    }
}
