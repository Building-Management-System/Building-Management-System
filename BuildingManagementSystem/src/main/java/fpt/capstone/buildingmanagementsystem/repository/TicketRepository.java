package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {
    Optional<Ticket> findByTicketId(String ticketId);
    @Transactional
    @Modifying
    @Query(value = "UPDATE ticket SET update_date = :update_date" +
            " where ticket_id = :ticket_id", nativeQuery = true)
    int updateTicketTime( @Param(value = "update_date") String update_date, @Param(value = "ticket_id") String ticket_id);
}
