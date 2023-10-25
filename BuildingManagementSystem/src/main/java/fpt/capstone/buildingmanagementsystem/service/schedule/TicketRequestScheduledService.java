package fpt.capstone.buildingmanagementsystem.service.schedule;

import fpt.capstone.buildingmanagementsystem.model.entity.RequestTicket;
import fpt.capstone.buildingmanagementsystem.model.entity.Ticket;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.RequestStatus;
import fpt.capstone.buildingmanagementsystem.repository.RequestTicketRepository;
import fpt.capstone.buildingmanagementsystem.repository.TicketRepository;
import fpt.capstone.buildingmanagementsystem.until.Until;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.Collectors;

import static fpt.capstone.buildingmanagementsystem.model.enumEnitty.RequestStatus.EXECUTING;

@Component
public class TicketRequestScheduledService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    RequestTicketRepository requestTicketRepository;

    private static final Logger logger = LoggerFactory.getLogger(TicketRequestScheduledService.class);

    private static final int day = 1000 * 60 * 60 * 24;
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

    //(cron = "* /* * * **")
    @Scheduled(fixedDelay = 10000)
    public void closeTicketUpTime() {
        closeTicketWithAnsweredRequests();
    }

    public void closeTicketWithAnsweredRequests() {
        List<Ticket> tickets = ticketRepository.findAll();
        tickets.forEach(ticket -> {
            List<RequestTicket> requestTickets = requestTicketRepository.findByTicketRequest(ticket)
                    .stream().filter(requestTicket -> requestTicket.getStatus().equals(EXECUTING) ||
                            requestTicket.getStatus().equals(RequestStatus.PENDING))
                    .collect(Collectors.toList());
            if (requestTickets.isEmpty()) {
                try {
                    Date updateDate = formatter.parse(ticket.getUpdateDate());
                    Date instantDate = formatter.parse(Until.generateRealTime());
                    //3 * day
                    if (instantDate.getTime() - updateDate.getTime() >= day) {
                        ticket.setStatus(false);
                        logger.info("run-done");
                    }
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        ticketRepository.saveAll(tickets);
    }
}
