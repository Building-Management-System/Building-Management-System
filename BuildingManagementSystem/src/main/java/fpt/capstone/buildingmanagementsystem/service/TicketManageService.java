package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.model.dto.RequestTicketDto;
import fpt.capstone.buildingmanagementsystem.model.dto.TicketDto;
import fpt.capstone.buildingmanagementsystem.model.dto.TicketRequestDto;
import fpt.capstone.buildingmanagementsystem.model.response.RequestTicketResponse;
import fpt.capstone.buildingmanagementsystem.model.response.TicketRequestResponse;
import fpt.capstone.buildingmanagementsystem.model.response.TicketRequestResponseV2;
import fpt.capstone.buildingmanagementsystem.repository.TicketRepositoryv2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class TicketManageService {

    @Autowired
    private TicketRepositoryv2 ticketRepository;

    public List<TicketRequestResponse> getAllTickets() {
        List<TicketRequestResponse> ticketRequestResponses = new ArrayList<>();
        Map<String, List<TicketDto>> ticketDtos = ticketRepository.getAllTicketRequest()
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
        Map<String, List<TicketRequestDto>> ticketDtos = ticketRepository.getTicketRequestv2()
                .stream()
                .filter(ticketRequestDto -> ticketRequestDto.getSenderId().equals(senderId))
                .collect(groupingBy(TicketRequestDto::getTicketId, Collectors.toList()));

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
        return responseV2s;
    }
}
