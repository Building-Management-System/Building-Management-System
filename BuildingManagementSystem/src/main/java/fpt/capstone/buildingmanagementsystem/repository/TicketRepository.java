package fpt.capstone.buildingmanagementsystem.repository;

import fpt.capstone.buildingmanagementsystem.model.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, String> {
}
