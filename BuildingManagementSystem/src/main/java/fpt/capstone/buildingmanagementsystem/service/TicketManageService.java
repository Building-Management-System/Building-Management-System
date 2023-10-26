package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.exception.NotFound;
import fpt.capstone.buildingmanagementsystem.model.dto.RequestTicketDto;
import fpt.capstone.buildingmanagementsystem.model.dto.TicketDto;
import fpt.capstone.buildingmanagementsystem.model.dto.TicketRequestDto;
import fpt.capstone.buildingmanagementsystem.model.entity.RequestTicket;
import fpt.capstone.buildingmanagementsystem.model.entity.User;
import fpt.capstone.buildingmanagementsystem.model.request.ChangeReceiveIdRequest;
import fpt.capstone.buildingmanagementsystem.model.response.RequestTicketResponse;
import fpt.capstone.buildingmanagementsystem.model.response.TicketRequestResponse;
import fpt.capstone.buildingmanagementsystem.model.response.TicketRequestResponseV2;
import fpt.capstone.buildingmanagementsystem.repository.RequestMessageRepository;
import fpt.capstone.buildingmanagementsystem.repository.RequestTicketRepository;
import fpt.capstone.buildingmanagementsystem.repository.TicketRepository;
import fpt.capstone.buildingmanagementsystem.repository.TicketRepositoryv2;
import fpt.capstone.buildingmanagementsystem.repository.UserRepository;
import fpt.capstone.buildingmanagementsystem.until.Until;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static fpt.capstone.buildingmanagementsystem.model.enumEnitty.RequestStatus.EXECUTING;
import static java.util.stream.Collectors.groupingBy;

@Service
public class TicketManageService {

    @Autowired
    private TicketRepositoryv2 ticketRepositoryv2;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    TicketRepository ticketRepository;
    @Autowired
    RequestTicketRepository requestTicketRepository;
    @Autowired
    RequestMessageRepository requestMessageRepository;

    public List<TicketRequestResponse> getAllTickets() {
        List<TicketRequestResponse> ticketRequestResponses = new ArrayList<>();
        Map<String, List<TicketDto>> ticketDtos = ticketRepositoryv2.getAllTicketRequest()
                .stream()
                .collect(groupingBy(TicketDto::getTicketId, Collectors.toList()));

        ticketDtos.forEach((s, tickets) -> {
            List<RequestTicketDto> requestTicketDtos = new ArrayList<>();
            TicketRequestResponse ticketResponse = new TicketRequestResponse();
            BeanUtils.copyProperties(tickets.get(0), ticketResponse);
            tickets.forEach(ticketDto -> {
                RequestTicketDto requestTicketDto = new RequestTicketDto();
                BeanUtils.copyProperties(ticketDto, requestTicketDto);
                requestTicketDto.setRequestStatus(ticketDto.getRequestStatus());
                requestTicketDtos.add(requestTicketDto);
            });
            ticketResponse.setRequestTicketDtos(requestTicketDtos);
            ticketRequestResponses.add(ticketResponse);
        });
        return ticketRequestResponses;
    }

    public List<TicketRequestResponseV2> getAllTicketsBySenderId(String senderId) {
        List<TicketRequestResponseV2> responseV2s = new ArrayList<>();
        Map<String, List<TicketRequestDto>> ticketDtos = ticketRepositoryv2.getTicketRequestv2()
                .stream()
                .filter(ticketRequestDto -> ticketRequestDto.getSenderId().equals(senderId))
                .collect(groupingBy(TicketRequestDto::getTicketId, Collectors.toList()));

        executeListTicketResponse(responseV2s, ticketDtos);
        return responseV2s.stream()
                .sorted((Comparator.comparing(TicketRequestResponseV2::getUpdateDate).reversed()))
                .collect(Collectors.toList());
    }

    private void executeListTicketResponse(List<TicketRequestResponseV2> responseV2s, Map<String, List<TicketRequestDto>> ticketDtos) {
        ticketDtos.forEach((s, tickets) -> {
            List<RequestTicketResponse> requestTickets = new ArrayList<>();
            TicketRequestResponseV2 ticketResponse = new TicketRequestResponseV2();
            BeanUtils.copyProperties(tickets.get(0), ticketResponse);
            tickets.forEach(ticketDto -> {
                RequestTicketResponse requestTicket = new RequestTicketResponse();
                BeanUtils.copyProperties(ticketDto, requestTicket);
                requestTicket.setRequestStatus(ticketDto.getRequestStatus());
                requestTickets.add(requestTicket);
            });
            ticketResponse.setRequestTickets(requestTickets);
            responseV2s.add(ticketResponse);
        });
    }

    public List<TicketRequestResponseV2> getAllTicketsByHr() {
        List<TicketRequestResponseV2> responseV2s = new ArrayList<>();
        Map<String, List<TicketRequestDto>> ticketDtos = ticketRepositoryv2.getTicketRequestByHr()
                .stream()
                .collect(groupingBy(TicketRequestDto::getTicketId, Collectors.toList()));

        return getTicketRequestResponse(responseV2s, ticketDtos);
    }

    public List<TicketRequestResponseV2> getAllTicketsBySecurity() {
        List<TicketRequestResponseV2> responseV2s = new ArrayList<>();
        Map<String, List<TicketRequestDto>> ticketDtos = ticketRepositoryv2.getTicketRequestBySecurity()
                .stream()
                .collect(groupingBy(TicketRequestDto::getTicketId, Collectors.toList()));
        return getTicketRequestResponse(responseV2s, ticketDtos);
    }

    public List<TicketRequestResponseV2> getAllTicketsByAdmin() {
        List<TicketRequestResponseV2> responseV2s = new ArrayList<>();
        Map<String, List<TicketRequestDto>> ticketDtos = ticketRepositoryv2.getTicketRequestByAdmin()
                .stream()
                .collect(groupingBy(TicketRequestDto::getTicketId, Collectors.toList()));
        return getTicketRequestResponse(responseV2s, ticketDtos);
    }

    public List<TicketRequestResponseV2> getAllTicketByDepartmentManager(String departmentName) {
        List<TicketRequestResponseV2> responseV2s = new ArrayList<>();
        List<User> manager = userRepository.getManagerByDepartment(departmentName);
        if (manager.isEmpty()) return new ArrayList<>();
        Map<String, List<TicketRequestDto>> ticketDtos = ticketRepositoryv2.getTicketRequestByDepartmentManager(manager.get(0).getUserId())
                .stream()
                .collect(groupingBy(TicketRequestDto::getTicketId, Collectors.toList()));
        return getTicketRequestResponse(responseV2s, ticketDtos);
    }


    private List<TicketRequestResponseV2> getTicketRequestResponse(List<TicketRequestResponseV2> responseV2s, Map<String, List<TicketRequestDto>> ticketDtos) {
        executeListTicketResponse(responseV2s, ticketDtos);
        return responseV2s.stream()
                .sorted((Comparator.comparing(TicketRequestResponseV2::getUpdateDate)).reversed())
                .sorted((Comparator.comparing(TicketRequestResponseV2::isStatus)).reversed())
                .collect(Collectors.toList());

    }
    @Transactional
    public boolean changeReceiveId(ChangeReceiveIdRequest changeReceiveIdRequest) {
        if (changeReceiveIdRequest.getReceiverId() != null &&
                changeReceiveIdRequest.getRequestId() != null) {
            if (!userRepository.findByUserId(changeReceiveIdRequest.getReceiverId()).isPresent()) {
                throw new NotFound("receiver_id_not_found");
            }
            String time = Until.generateRealTime();
            requestMessageRepository.updateTicketRequestTime(changeReceiveIdRequest.getReceiverId(), time, changeReceiveIdRequest.getRequestId());
            RequestTicket requestTicket = requestTicketRepository.findByRequestId(changeReceiveIdRequest.getRequestId()).get();
            requestTicket.setStatus(EXECUTING);
            requestTicket.setUpdateDate(time);
            requestTicket.setRequestId(changeReceiveIdRequest.getRequestId());
            requestTicketRepository.save(requestTicket);
            if (requestTicketRepository.findByRequestId(changeReceiveIdRequest.getRequestId()).isPresent()) {
                String ticketId = requestTicketRepository.findByRequestId(changeReceiveIdRequest.getRequestId()).get().getTicketRequest().getTicketId();
                ticketRepository.updateTicketTime(time, ticketId);
                return true;
            } else {
                throw new NotFound("request_ticket_not_found");
            }
        } else {
            throw new BadRequest("request_fail");
        }
    }
}
