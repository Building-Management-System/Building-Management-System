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

import java.util.List;
import java.util.Objects;
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
    public boolean getOtherFormUser(SendOtherFormRequest sendOtherFormRequest) {
        try {
            if (sendOtherFormRequest.getContent() != null&&
                    sendOtherFormRequest.getDepartmentId() != null&&
                    sendOtherFormRequest.getTitle() != null
            ) {
                Optional<User> send_user= userRepository.findByUserId(sendOtherFormRequest.getUserId());
                Optional<Department> department= departmentRepository.findByDepartmentId(sendOtherFormRequest.getDepartmentId());
                if(send_user.isPresent()&&department.isPresent()) {
                    String id_ticket = "OR_" + Until.generateId();
                    String id_request_ticket = "OR_" + Until.generateId();
                    //
                    Ticket ticket = Ticket.builder().ticketId(id_ticket).topic(OTHER_REQUEST).status(false).createDate(Until.generateRealTime())
                            .updateDate(Until.generateRealTime()).build();
                    ticketRepository.save(ticket);
                    //
                    saveOtherRequest(sendOtherFormRequest, send_user, department, id_request_ticket, ticket);
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
    public boolean getOtherFormUserExistTicket(SendOtherFormRequest sendOtherFormRequest) {
        try {
            if (sendOtherFormRequest.getContent() != null&&
                    sendOtherFormRequest.getDepartmentId() != null&&
                    sendOtherFormRequest.getTitle() != null&&
                    sendOtherFormRequest.getTicketId()!=null
            ) {
                Optional<User> send_user= userRepository.findByUserId(sendOtherFormRequest.getUserId());
                Optional<Department> department= departmentRepository.findByDepartmentId(sendOtherFormRequest.getDepartmentId());
                Optional<Ticket> ticket = ticketRepository.findByTicketId(sendOtherFormRequest.getTicketId());
                if(send_user.isPresent()&&department.isPresent()&&ticket.isPresent()) {
                    String id_request_ticket = "OR_" + Until.generateId();
                    saveOtherRequest(sendOtherFormRequest, send_user, department, id_request_ticket, ticket.get());
                    ticketRepository.updateTicketTime(Until.generateRealTime(),sendOtherFormRequest.getTicketId());
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
    public boolean getOtherFormUserExistRequest(SendOtherFormRequest sendOtherFormRequest) {
        try {
            if (sendOtherFormRequest.getContent() != null&&
                    sendOtherFormRequest.getDepartmentId() != null&&
                    sendOtherFormRequest.getRequestId()!=null
            ) {
                Optional<User> send_user= userRepository.findByUserId(sendOtherFormRequest.getUserId());
                Optional<Department> department= departmentRepository.findByDepartmentId(sendOtherFormRequest.getDepartmentId());
                Optional<RequestTicket> requestTicket = requestTicketRepository.findByRequestId(sendOtherFormRequest.getRequestId());
                if(send_user.isPresent()&&department.isPresent()&&requestTicket.isPresent()) {
                        List<RequestMessage> requestMessageOptional = requestMessageRepository.findByRequest(requestTicket.get());
                        String senderId = requestMessageOptional.get(0).getSender().getUserId();
                    if(requestMessageOptional.get(0).getReceiver()!=null) {
                        String receiverId = requestMessageOptional.get(0).getReceiver().getUserId();
                        if (Objects.equals(sendOtherFormRequest.getUserId(), senderId)) {
                            sendOtherFormRequest.setReceivedId(receiverId);
                        }else {
                            sendOtherFormRequest.setReceivedId(senderId);
                        }
                    }
                    String time=Until.generateRealTime();
                    saveOtherMessage(sendOtherFormRequest, send_user, department, requestTicket.get());
                    ticketRepository.updateTicketTime(time,requestTicket.get().getTicketRequest().getTicketId());
                    requestTicketRepository.updateTicketRequestTime(time,sendOtherFormRequest.getRequestId());
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
    private void saveOtherRequest(SendOtherFormRequest sendOtherFormRequest, Optional<User> send_user, Optional<Department> department, String id_request_ticket, Ticket ticket) {
        RequestTicket requestTicket = RequestTicket.builder().requestId(id_request_ticket).createDate(Until.generateRealTime())
                .updateDate(Until.generateRealTime())
                .status(PENDING).ticketRequest(ticket).title(sendOtherFormRequest.getTitle()).user(send_user.get()).build();
        saveOtherMessage(sendOtherFormRequest, send_user, department, requestTicket);
    }

    private void saveOtherMessage(SendOtherFormRequest sendOtherFormRequest, Optional<User> send_user, Optional<Department> department, RequestTicket requestTicket) {
        RequestMessage requestMessage = RequestMessage.builder().createDate(Until.generateRealTime())
                .updateDate(Until.generateRealTime())
                .sender(send_user.get()).request(requestTicket).department(department.get()).build();
        if (sendOtherFormRequest.getReceivedId() != null){
            Optional<User> receive_user = userRepository.findByUserId(sendOtherFormRequest.getReceivedId());
            requestMessage.setReceiver(receive_user.get());
        }
        OtherRequest otherRequest=otherRequestMapper.convert(sendOtherFormRequest);
        otherRequest.setTopic(OTHER_REQUEST);
        otherRequest.setRequestMessage(requestMessage);
        otherRequest.setStatus(false);
        requestTicketRepository.save(requestTicket);
        requestMessageRepository.save(requestMessage);
        otherRequestFormRepository.save(otherRequest);
    }
}
