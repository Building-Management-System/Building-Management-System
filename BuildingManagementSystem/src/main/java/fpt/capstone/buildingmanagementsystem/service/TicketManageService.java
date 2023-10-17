package fpt.capstone.buildingmanagementsystem.service;

import fpt.capstone.buildingmanagementsystem.model.request.TicketAttendanceRequest;
import fpt.capstone.buildingmanagementsystem.repository.AttendanceRequestFormRepository;
import fpt.capstone.buildingmanagementsystem.repository.RequestMessageRepository;
import fpt.capstone.buildingmanagementsystem.repository.RequestTicketRepository;
import fpt.capstone.buildingmanagementsystem.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class TicketManageService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private RequestTicketRepository requestTicketRepository;

    @Autowired
    private RequestMessageRepository requestMessageRepository;

    @Autowired
    private AttendanceRequestFormRepository attendanceRequestRepository;

//    public ResponseEntity<?> createNewAttendanceRequestTicket(TicketAttendanceRequest ticketAttendanceRequest) {
//
//    }
//
//    private String generateTicketId(String topic) {
//        switch (topic) {
//            case :
//                break;
//        }
//    }
}
