package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.mapper.OtherRequestMapper;
import fpt.capstone.buildingmanagementsystem.model.entity.*;
import fpt.capstone.buildingmanagementsystem.model.entity.requestForm.LeaveRequestForm;
import fpt.capstone.buildingmanagementsystem.model.entity.requestForm.OtherRequest;
import fpt.capstone.buildingmanagementsystem.model.request.SendLeaveFormRequest;
import fpt.capstone.buildingmanagementsystem.model.request.SendOtherFormRequest;
import fpt.capstone.buildingmanagementsystem.repository.*;
import fpt.capstone.buildingmanagementsystem.until.Until;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import static fpt.capstone.buildingmanagementsystem.model.enumEnitty.RequestStatus.PENDING;
import static fpt.capstone.buildingmanagementsystem.model.enumEnitty.TopicEnum.OTHER_REQUEST;

@Service
public class RequestOtherService {
    @Autowired
    OtherRequestMapper otherRequestMapper;
    @Autowired
    OtherRequestFormRepository otherRequestFormRepository;
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
    public boolean getLeaveFormUser(SendOtherFormRequest sendOtherFormRequest) {
        try {
            if (sendOtherFormRequest.getContent() != null&&
                    sendOtherFormRequest.getDepartmentId() != null&&
                    sendOtherFormRequest.getTitle() != null&&
                    sendOtherFormRequest.getReceivedId() != null
            ) {
                Optional<User> send_user= userRepository.findByUserId(sendOtherFormRequest.getUserId());
                Optional<User> receive_user= userRepository.findByUserId(sendOtherFormRequest.getReceivedId());
                Optional<Department> department= departmentRepository.findByDepartmentId(sendOtherFormRequest.getDepartmentId());
                if(send_user.isPresent()&&receive_user.isPresent()&&department.isPresent()) {
                    String id_ticket = "OR_" + Until.generateId();
                    String id_request_ticket = "OR_" + Until.generateId();
                    //
                    Ticket ticket = Ticket.builder().ticketId(id_ticket).topic(OTHER_REQUEST).status(false).createDate(Until.generateRealTime().toString())
                            .updateDate(Until.generateRealTime().toString()).build();
                    //
                    RequestTicket requestTicket = RequestTicket.builder().requestId(id_request_ticket).createDate(Until.generateRealTime().toString())
                            .updateDate(Until.generateRealTime().toString())
                            .status(PENDING).ticketRequest(ticket).title(sendOtherFormRequest.getTitle()).user(send_user.get()).build();
                    //
                    RequestMessage requestMessage= RequestMessage.builder().createDate(Until.generateRealTime().toString())
                            .updateDate(Until.generateRealTime().toString())
                            .sender(send_user.get()).receiver(receive_user.get()).request(requestTicket).department(department.get()).build();
                    //
                    OtherRequest otherRequest=otherRequestMapper.convert(sendOtherFormRequest);
                    otherRequest.setTopic(OTHER_REQUEST);
                    otherRequest.setRequestMessage(requestMessage);
                    otherRequest.setStatus(false);
                    ticketRepository.save(ticket);
                    requestTicketRepository.save(requestTicket);
                    requestMessageRepository.save(requestMessage);
                    otherRequestFormRepository.save(otherRequest);
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
