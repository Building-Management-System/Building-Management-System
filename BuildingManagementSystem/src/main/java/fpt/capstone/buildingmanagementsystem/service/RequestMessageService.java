package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.model.entity.RequestMessage;
import fpt.capstone.buildingmanagementsystem.model.entity.RequestTicket;
import fpt.capstone.buildingmanagementsystem.model.response.AttendanceAndOtherFormResponse;
import fpt.capstone.buildingmanagementsystem.model.response.AttendanceFormResponse;
import fpt.capstone.buildingmanagementsystem.model.response.OtherRequestResponse;
import fpt.capstone.buildingmanagementsystem.model.response.RequestMessageResponse;
import fpt.capstone.buildingmanagementsystem.repository.AttendanceRequestFormRepository;
import fpt.capstone.buildingmanagementsystem.repository.OtherRequestFormRepository;
import fpt.capstone.buildingmanagementsystem.repository.RequestMessageRepository;
import fpt.capstone.buildingmanagementsystem.repository.RequestTicketRepository;
import org.springframework.beans.BeanUtils;
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

    public List<Map<RequestMessageResponse, Object>> getAllAttendanceMessageByRequestId(String requestId) {
        RequestTicket requestTicket = requestTicketRepository.findById(requestId)
                .orElseThrow(() -> new BadRequest("Not_found_request_ticket"));

        List<RequestMessage> requestMessages = requestMessageRepository.findByRequest(requestTicket);

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

        List<OtherRequestResponse> otherRequests = otherRequestFormRepository.findByRequestMessageIn(requestMessages)
                .stream().map(form -> new OtherRequestResponse(
                        form.getOtherRequestId(),
                        form.getContent(),
                        form.getRequestMessage().getRequestMessageId(),
                        form.getTopic()
                )).collect(Collectors.toList());

        List<RequestMessageResponse> messageResponses = new ArrayList<>();
        requestMessages.forEach(requestMessage -> {
            RequestMessageResponse messageResponse = new RequestMessageResponse();
            BeanUtils.copyProperties(requestMessage, messageResponse);
            messageResponse.setSenderId(requestMessage.getSender().getUserId());
            messageResponse.setReceiverId(requestMessage.getReceiver().getUserId());
            messageResponse.setRequestId(requestMessage.getRequest().getRequestId());
            messageResponse.setReceiverDepartment(requestMessage.getDepartment());
            messageResponses.add(messageResponse);
        });

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
                        return new AbstractMap.SimpleImmutableEntry<>(o, new AttendanceAndOtherFormResponse(o, attendanceMap.get(o.getRequestMessageId())));
                    }
                    return new AbstractMap.SimpleImmutableEntry<>(o,new AttendanceAndOtherFormResponse(o,  otherMap.get(o.getRequestMessageId())));
                }).collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (left, right) -> right,
                        LinkedHashMap::new
                ));
        return List.of(responseObjectMap);
    }
}
