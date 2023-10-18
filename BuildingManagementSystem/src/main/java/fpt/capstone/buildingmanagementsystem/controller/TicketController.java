package fpt.capstone.buildingmanagementsystem.controller;

import fpt.capstone.buildingmanagementsystem.model.response.TicketRequestResponse;
import fpt.capstone.buildingmanagementsystem.service.TicketManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class TicketController {

    @Autowired
    private TicketManageService ticketManageService;

    @GetMapping("/ticketRequest")
    public List<TicketRequestResponse> getAllTicketAndRequest() {
        return ticketManageService.getAllTickets();
    }

}
