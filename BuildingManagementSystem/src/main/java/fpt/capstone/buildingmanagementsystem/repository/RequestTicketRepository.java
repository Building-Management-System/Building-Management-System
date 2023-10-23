package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.RequestTicket;
import fpt.capstone.buildingmanagementsystem.model.entity.Ticket;
import fpt.capstone.buildingmanagementsystem.model.enumEnitty.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RequestTicketRepository extends JpaRepository<RequestTicket, String> {
    Optional<RequestTicket> findByRequestId(String ticketId);
    @Transactional
    @Modifying
    @Query(value = "UPDATE request_ticket SET update_date = :update_date" +
            " where request_id = :request_id", nativeQuery = true)
    int updateTicketRequestTime(@Param(value = "update_date") String update_date, @Param(value = "request_id") String request_id);


}
