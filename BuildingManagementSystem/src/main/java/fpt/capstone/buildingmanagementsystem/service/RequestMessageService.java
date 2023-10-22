package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.model.entity.RequestMessage;
import fpt.capstone.buildingmanagementsystem.model.entity.RequestTicket;
import fpt.capstone.buildingmanagementsystem.model.entity.RoomBookingFormRoom;
import fpt.capstone.buildingmanagementsystem.model.entity.requestForm.RoomBookingRequestForm;
import fpt.capstone.buildingmanagementsystem.model.response.AttendanceFormResponse;
import fpt.capstone.buildingmanagementsystem.model.response.OtherRequestResponse;
import fpt.capstone.buildingmanagementsystem.model.response.RequestFormResponse;
import fpt.capstone.buildingmanagementsystem.model.response.RequestMessageResponse;
import fpt.capstone.buildingmanagementsystem.model.response.RoomBookingResponseV2;
import fpt.capstone.buildingmanagementsystem.repository.AttendanceRequestFormRepository;
import fpt.capstone.buildingmanagementsystem.repository.OtherRequestFormRepository;
import fpt.capstone.buildingmanagementsystem.repository.RequestMessageRepository;
import fpt.capstone.buildingmanagementsystem.repository.RequestTicketRepository;
import fpt.capstone.buildingmanagementsystem.repository.RoomBookingFormRepository;
import fpt.capstone.buildingmanagementsystem.repository.RoomBookingFormRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RequestMessageService {

    @Autowired
    private RequestTicketRepository requestTicketRepository;

    @Autowired
    private RequestMessageRepository requestMessageRepository;

    @Autowired
    private OtherRequestFormRepository otherRequestFormRepository;

    @Autowired
    private AttendanceRequestFormRepository attendanceRequestFormRepository;

    @Autowired
    private RoomBookingFormRepository roomBookingFormRepository;

    @Autowired
    private RoomBookingFormRoomRepository roomFormRoomRepository;

    public List<Map<RequestMessageResponse, Object>> getAllAttendanceMessageByRequestId(String requestId) {
        RequestTicket requestTicket = requestTicketRepository.findById(requestId)
                .orElseThrow(() -> new BadRequest("Not_found_request_ticket"));

        List<RequestMessage> requestMessages = requestMessageRepository.findByRequest(requestTicket);

        List<RequestMessageResponse> messageResponses = requestMessages.stream()
                .map(requestMessage -> new RequestMessageResponse(
                        requestMessage.getRequestMessageId(),
                        requestMessage.getCreateDate(),
                        requestMessage.getAttachmentMessageId(),
                        requestMessage.getSender().getUserId(),
                        requestMessage.getSender().getFirstName(),
                        requestMessage.getSender().getLastName(),
                        requestMessage.getReceiver().getUserId(),
                        requestMessage.getReceiver().getFirstName(),
                        requestMessage.getReceiver().getLastName(),
                        requestMessage.getRequest().getRequestId(),
                        requestMessage.getDepartment()
                ))
                .collect(Collectors.toList());

        List<OtherRequestResponse> otherRequests = otherRequestFormRepository.findByRequestMessageIn(requestMessages)
                .stream().map(form -> new OtherRequestResponse(
                        form.getOtherRequestId(),
                        form.getContent(),
                        form.getRequestMessage().getRequestMessageId(),
                        form.getTopic()
                )).collect(Collectors.toList());

        List<AttendanceFormResponse> attendanceRequests = attendanceRequestFormRepository.findByRequestMessageIn(requestMessages)
                .stream().map(form -> new AttendanceFormResponse(
                        form.getAttendanceRequestId(),
                        form.getManualDate(),
                        form.getManualFirstEntry(),
                        form.getManualLastExit(),
                        form.getContent(),
                        form.getRequestMessage().getRequestMessageId(),
                        form.getTopic()
                ))
                .collect(Collectors.toList());

        Map<String, AttendanceFormResponse> attendanceMap = attendanceRequests.stream()
                .collect(Collectors.toMap(AttendanceFormResponse::getRequestMessageId, Function.identity()));

        Map<String, OtherRequestResponse> otherMap = otherRequests.stream()
                .collect(Collectors.toMap(OtherRequestResponse::getRequestMessageId, Function.identity()));

        Map<RequestMessageResponse, Object> responseObjectMap = messageResponses.stream()
                .collect(Collectors.toMap(Function.identity(), messageResponse -> messageResponse));

        responseObjectMap = responseObjectMap.keySet().stream()
                .sorted(Comparator.comparing(RequestMessageResponse::getCreateDate))
                .map(o -> {
                    if (attendanceMap.containsKey(o.getRequestMessageId())) {
                        return new AbstractMap.SimpleImmutableEntry<>(o, new RequestFormResponse(o, attendanceMap.get(o.getRequestMessageId())));
                    }
                    return new AbstractMap.SimpleImmutableEntry<>(o, new RequestFormResponse(o, otherMap.get(o.getRequestMessageId())));
                }).collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (left, right) -> right,
                        LinkedHashMap::new
                ));
        return List.of(responseObjectMap);
    }

    public List<Map<RequestMessageResponse, Object>> getAllRoomBookingMessageByRequestId(String requestId) {
        RequestTicket requestTicket = requestTicketRepository.findById(requestId)
                .orElseThrow(() -> new BadRequest("Not_found_request_ticket"));

        List<RequestMessage> requestMessages = requestMessageRepository.findByRequest(requestTicket);

        List<RequestMessageResponse> messageResponses = new ArrayList<>();

        requestMessages.forEach(requestMessage -> {
            RequestMessageResponse messageResponse = new RequestMessageResponse();
            messageResponse.setRequestMessageId(requestMessage.getRequestMessageId());
            messageResponse.setCreateDate(requestMessage.getCreateDate());
            messageResponse.setAttachmentMessageId(requestMessage.getAttachmentMessageId());
            messageResponse.setSenderId(requestMessage.getSender().getUserId());
            messageResponse.setSenderFirstName(requestMessage.getSender().getFirstName());
            messageResponse.setSenderLastName(requestMessage.getSender().getLastName());
            messageResponse.setRequestId(requestMessage.getRequest().getRequestId());
            messageResponse.setReceiverDepartment(requestMessage.getDepartment());
            if (requestMessage.getReceiver() != null) {
                messageResponse.setReceiverId(requestMessage.getReceiver().getUserId());
                messageResponse.setReceiverFirstName(requestMessage.getReceiver().getFirstName());
                messageResponse.setReceiverLastName(requestMessage.getReceiver().getLastName());
            }
            messageResponses.add(messageResponse);
        });

        List<OtherRequestResponse> otherRequests = otherRequestFormRepository.findByRequestMessageIn(requestMessages)
                .stream().map(form -> new OtherRequestResponse(
                        form.getOtherRequestId(),
                        form.getContent(),
                        form.getRequestMessage().getRequestMessageId(),
                        form.getTopic()
                )).collect(Collectors.toList());


        List<RoomBookingRequestForm> roomBookingRequestForms = roomBookingFormRepository.findByRequestMessageIn(requestMessages);

        List<RoomBookingFormRoom> roomBookingFormRooms = roomFormRoomRepository.findByRoomRequestFormIn(roomBookingRequestForms);

        Map<String, RoomBookingFormRoom> mapRoomForm = roomBookingFormRooms.stream()
                .collect(Collectors.toMap(rr -> rr.getRoomRequestForm().getRoomBookingRequestId(), Function.identity()));

        Map<RoomBookingFormRoom, RoomBookingRequestForm> requestFormMap = roomBookingRequestForms.stream()
                .collect(Collectors.toMap(rqForm -> mapRoomForm.get(rqForm.getRoomBookingRequestId()), Function.identity()));

        List<RoomBookingResponseV2> roomBookingResponses = new ArrayList<>();
        requestFormMap.forEach((k, v) -> {
            RoomBookingResponseV2 roomBookingResponse = RoomBookingResponseV2.builder()
                    .roomBookingRequestId(v.getRoomBookingRequestId())
                    .title(v.getTitle())
                    .content(v.getContent())
                    .bookingDate(v.getBookingDate())
                    .startDate(v.getStartTime())
                    .endDate(v.getEndTime())
                    .senderDepartment(v.getDepartmentSender())
                    .topic(v.getTopic())
                    .roomId(k.getRoom().getRoomId())
                    .roomName(k.getRoom().getRoomName())
                    .requestMessageId(v.getRequestMessage().getRequestMessageId())
                    .build();
            roomBookingResponses.add(roomBookingResponse);

        });

        Map<String, RoomBookingResponseV2> roomBookingMap = roomBookingResponses.stream()
                .collect(Collectors.toMap(RoomBookingResponseV2::getRequestMessageId, Function.identity()));

        Map<String, OtherRequestResponse> otherMap = otherRequests.stream()
                .collect(Collectors.toMap(OtherRequestResponse::getRequestMessageId, Function.identity()));

        Map<RequestMessageResponse, Object> responseObjectMap = messageResponses.stream()
                .collect(Collectors.toMap(Function.identity(), messageResponse -> messageResponse));

        responseObjectMap = responseObjectMap.keySet().stream()
                .sorted(Comparator.comparing(RequestMessageResponse::getCreateDate))
                .map(o -> {
                    if (roomBookingMap.containsKey(o.getRequestMessageId())) {
                        return new AbstractMap.SimpleImmutableEntry<>(o, new RequestFormResponse(o, roomBookingMap.get(o.getRequestMessageId())));
                    }
                    return new AbstractMap.SimpleImmutableEntry<>(o, new RequestFormResponse(o, otherMap.get(o.getRequestMessageId())));
                }).collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (left, right) -> right,
                        LinkedHashMap::new
                ));
        return List.of(responseObjectMap);

    }

}
