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

import java.util.Optional;

import static fpt.capstone.buildingmanagementsystem.model.enumEnitty.RequestStatus.PENDING;
import static fpt.capstone.buildingmanagementsystem.model.enumEnitty.TopicEnum.LEAVE_REQUEST;
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
            if (sendLeaveFormRequest.getContent() != null&&
                    sendLeaveFormRequest.getDepartmentId() != null&&
                    sendLeaveFormRequest.getTitle() != null&&
                    sendLeaveFormRequest.getReceivedId() != null&&
                    sendLeaveFormRequest.getFromDate()!= null&&
                    sendLeaveFormRequest.getToDate()!= null
            ) {
                Optional<User> send_user= userRepository.findByUserId(sendLeaveFormRequest.getUserId());
                Optional<User> receive_user= userRepository.findByUserId(sendLeaveFormRequest.getReceivedId());
                Optional<Department> department= departmentRepository.findByDepartmentId(sendLeaveFormRequest.getDepartmentId());
                if(send_user.isPresent()&&receive_user.isPresent()&&department.isPresent()) {
                    String id_ticket = "LV_" + Until.generateId();
                    String id_request_ticket = "LV_" + Until.generateId();
                    //
                    Ticket ticket = Ticket.builder().ticketId(id_ticket).topic(LEAVE_REQUEST).status(false).createDate(Until.generateRealTime().toString())
                            .updateDate(Until.generateRealTime().toString()).build();
                    //
                    RequestTicket requestTicket = RequestTicket.builder().requestId(id_request_ticket).createDate(Until.generateRealTime().toString())
                            .updateDate(Until.generateRealTime().toString())
                            .status(PENDING).ticketRequest(ticket).title(sendLeaveFormRequest.getTitle()).user(send_user.get()).build();
                    //
                    RequestMessage requestMessage= RequestMessage.builder().createDate(Until.generateRealTime().toString())
                            .updateDate(Until.generateRealTime().toString())
                            .sender(send_user.get()).receiver(receive_user.get()).request(requestTicket).department(department.get()).build();
                    //
                    LeaveRequestForm leaveRequestForm = leaveRequestFormMapper.convert(sendLeaveFormRequest);
                    leaveRequestForm.setTopic(LEAVE_REQUEST);
                    leaveRequestForm.setStatus(false);
                    leaveRequestForm.setRequestMessage(requestMessage);
                    ticketRepository.save(ticket);
                    requestTicketRepository.save(requestTicket);
                    requestMessageRepository.save(requestMessage);
                    leaveRequestFormRepository.save(leaveRequestForm);
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
