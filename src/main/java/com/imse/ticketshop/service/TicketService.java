package com.imse.ticketshop.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public interface TicketService {

    void generateTickets(List<Map<String, String>> tableData, UUID uuid);
}
