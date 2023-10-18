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

import java.util.Optional;

import static fpt.capstone.buildingmanagementsystem.model.enumEnitty.RequestStatus.PENDING;
import static fpt.capstone.buildingmanagementsystem.model.enumEnitty.TopicEnum.ATTENDANCE_REQUEST;

@Service
public class RequestAttendanceFromService {
    @Autowired
    AttendanceRequestFormMapper attendanceRequestFormMapper;
    @Autowired
    AttendanceRequestFormRepository attendanceRequestFormRepository;
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
    public boolean getAttendanceUser(SendAttendanceFormRequest sendAttendanceFormRequest) {
        try {
            if (sendAttendanceFormRequest.getContent() != null&&
            sendAttendanceFormRequest.getDepartmentId() != null&&
            sendAttendanceFormRequest.getManualDate() != null&&
            sendAttendanceFormRequest.getTitle() != null&&
            sendAttendanceFormRequest.getManualLastExit() != null&&
            sendAttendanceFormRequest.getReceivedId() != null
            ) {
                Optional<User> send_user= userRepository.findByUserId(sendAttendanceFormRequest.getUserId());
                Optional<User> receive_user= userRepository.findByUserId(sendAttendanceFormRequest.getReceivedId());
                Optional<Department> department= departmentRepository.findByDepartmentId(sendAttendanceFormRequest.getDepartmentId());
                if(send_user.isPresent()&&receive_user.isPresent()&&department.isPresent()) {
                    String id_ticket = "AT_" + Until.generateId();
                    String id_request_ticket = "AT_" + Until.generateId();
                    //
                    Ticket ticket = Ticket.builder().ticketId(id_ticket).topic(ATTENDANCE_REQUEST).status(false).createDate(Until.generateRealTime().toString())
                            .updateDate(Until.generateRealTime().toString()).build();
                    //
                    RequestTicket requestTicket = RequestTicket.builder().requestId(id_request_ticket).createDate(Until.generateRealTime().toString())
                            .updateDate(Until.generateRealTime().toString())
                            .status(PENDING).ticketRequest(ticket).title(sendAttendanceFormRequest.getTitle()).user(send_user.get()).build();
                    //
                    RequestMessage requestMessage= RequestMessage.builder().createDate(Until.generateRealTime().toString())
                            .updateDate(Until.generateRealTime().toString())
                            .sender(send_user.get()).receiver(receive_user.get()).request(requestTicket).department(department.get()).build();
                    //
                    AttendanceRequestForm attendanceRequestForm = attendanceRequestFormMapper.convert(sendAttendanceFormRequest);
                    attendanceRequestForm.setTopic(ATTENDANCE_REQUEST);
                    attendanceRequestForm.setStatus(false);
                    attendanceRequestForm.setRequestMessage(requestMessage);
                    ticketRepository.save(ticket);
                    requestTicketRepository.save(requestTicket);
                    requestMessageRepository.save(requestMessage);
                    attendanceRequestFormRepository.save(attendanceRequestForm);
                    return true;
                }else {
                    throw new NotFound("not_found");
                }
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (ServerError e) {
            throw new ServerError("fail");
        }
    }
}
