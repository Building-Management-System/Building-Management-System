package fpt.capstone.buildingmanagementsystem;

import fpt.capstone.buildingmanagementsystem.exception.BadRequest;
import fpt.capstone.buildingmanagementsystem.model.entity.RequestTicket;
import fpt.capstone.buildingmanagementsystem.repository.RequestMessageRepository;
import fpt.capstone.buildingmanagementsystem.repository.RequestTicketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BuildingManagementSystemApplicationTests {
    @Autowired
    RequestMessageRepository requestMessageRepository;

    @Autowired
    private RequestTicketRepository requestTicketRepository;

    @Test
    void contextLoads() {
        RequestTicket requestTicket = requestTicketRepository.findById("5636ff5d-d2e7-4861-9d15-15eaea16c460")
                .orElseThrow(() -> new BadRequest("Not_found_request_ticket"));
        System.out.println(requestMessageRepository.findByRequest(requestTicket).size());
    }

}
