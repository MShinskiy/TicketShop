package com.imse.ticketshop.repository.rdbms;

import com.imse.ticketshop.entity.rdbms.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
}
