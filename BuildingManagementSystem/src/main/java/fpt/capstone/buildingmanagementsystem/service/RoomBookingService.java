package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.exception.ServerError;
import fpt.capstone.buildingmanagementsystem.model.entity.Department;
import fpt.capstone.buildingmanagementsystem.model.entity.RequestMessage;
import fpt.capstone.buildingmanagementsystem.model.entity.RequestTicket;
import fpt.capstone.buildingmanagementsystem.model.entity.Room;
import fpt.capstone.buildingmanagementsystem.model.entity.RoomBookingFormRoom;
import fpt.capstone.buildingmanagementsystem.model.entity.Ticket;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.entity.requestForm.RoomBookingRequestForm;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.RequestStatus;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.TopicEnum;
import fpt.capstone.buildingmanagementsystem.model.request.SendRoomBookingRequest;
import fpt.capstone.buildingmanagementsystem.model.response.RoomBookingResponse;
import fpt.capstone.buildingmanagementsystem.repository.DepartmentRepository;
import fpt.capstone.buildingmanagementsystem.repository.RequestMessageRepository;
import fpt.capstone.buildingmanagementsystem.repository.RequestTicketRepository;
import fpt.capstone.buildingmanagementsystem.repository.RoomBookingFormRepository;
import fpt.capstone.buildingmanagementsystem.repository.RoomBookingFormRepositoryV2;
import fpt.capstone.buildingmanagementsystem.repository.RoomBookingFormRoomRepository;
import fpt.capstone.buildingmanagementsystem.repository.RoomRepository;
import fpt.capstone.buildingmanagementsystem.repository.TicketRepository;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import fpt.capstone.buildingmanagementsystem.until.Until;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomBookingService {

    @Autowired
    private RoomRepository roomRepository;

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

    @Autowired
    RoomBookingFormRepository roomBookingFormRepository;

    @Autowired
    RoomBookingFormRepositoryV2 roomFormRepositoryV2;

    @Autowired
    RoomBookingFormRoomRepository roomBookingRoomRepository;


//    private static final TopicEnum ROOM_BOOKING = TopicEnum.ROOM_REQUEST;

    //    @Transactional
    public boolean getRoomBookingForm(SendRoomBookingRequest sendRoomBookingRequest) {
        try {
            if (sendRoomBookingRequest.getUserId() != null
                    && sendRoomBookingRequest.getDepartmentSenderId() != null
                    && sendRoomBookingRequest.getTitle() != null
                    && sendRoomBookingRequest.getStartTime() != null
                    && sendRoomBookingRequest.getEndTime() != null
                    && sendRoomBookingRequest.getDepartmentReceiverId() != null) {

                Room room = roomRepository.findById(sendRoomBookingRequest.getRoomId())
                        .orElseThrow(() -> new BadRequest("not_found_room"));
                User user = userRepository.findByUserId(sendRoomBookingRequest.getUserId())
                        .orElseThrow(() -> new BadRequest("not_found_user"));
                Department receiverDepartment = departmentRepository.findByDepartmentId(sendRoomBookingRequest.getDepartmentReceiverId())
                        .orElseThrow(() -> new BadRequest("not_found_receiver_department"));
                Department senderDepartment = departmentRepository.findByDepartmentId(sendRoomBookingRequest.getDepartmentSenderId())
                        .orElseThrow(() -> new BadRequest("not_found_sender_department"));


                String ticketId = "RB_" + Until.generateId();
                String requestTicketId = "RB_" + Until.generateId();

                Ticket ticket = Ticket.builder()
                        .ticketId(ticketId)
                        .topic(TopicEnum.ROOM_REQUEST)
                        .status(false)
                        .createDate(Until.generateRealTime())
                        .updateDate(Until.generateRealTime())
                        .build();

                RequestTicket requestTicket = RequestTicket.builder()
                        .requestId(requestTicketId)
                        .createDate(Until.generateRealTime())
                        .updateDate(Until.generateRealTime())
                        .status(RequestStatus.PENDING)
                        .ticketRequest(ticket)
                        .title(sendRoomBookingRequest.getTitle())
                        .user(user)
                        .build();

                RequestMessage requestMessage = RequestMessage.builder()
                        .createDate(Until.generateRealTime())
                        .updateDate(Until.generateRealTime())
                        .sender(user)
                        .request(requestTicket)
                        .department(receiverDepartment)
                        .build();

                if (sendRoomBookingRequest.getReceiverId() != null) {
                    User receiver = userRepository.findByUserId(sendRoomBookingRequest.getReceiverId())
                            .orElseThrow(() -> new NotFound("not_found_receiver"));
                    requestMessage.setReceiver(receiver);
                }

                RoomBookingRequestForm roomBookingForm = new RoomBookingRequestForm();
                BeanUtils.copyProperties(sendRoomBookingRequest, roomBookingForm);
                roomBookingForm.setStatus(true);
                roomBookingForm.setDepartmentSender(senderDepartment);
                roomBookingForm.setRequestMessage(requestMessage);
                roomBookingForm.setTopic(TopicEnum.ROOM_REQUEST);

                ticketRepository.save(ticket);
                requestTicketRepository.saveAndFlush(requestTicket);
                requestMessageRepository.saveAndFlush(requestMessage);
                roomBookingFormRepository.saveAndFlush(roomBookingForm);

                RoomBookingFormRoom roomBookingFormRoom = new RoomBookingFormRoom();
                roomBookingFormRoom.setRoomRequestForm(roomBookingForm);
                roomBookingFormRoom.setRoom(room);
                roomBookingRoomRepository.save(roomBookingFormRoom);
                return true;
            } else {
                throw new BadRequest("request_fail");
            }
        } catch (ServerError e) {
            throw new ServerError("fail");
        }
    }

    public List<RoomBookingResponse> getAllBookedRoom() {
        return roomFormRepositoryV2.getAllBookedRoom();
    }
}
