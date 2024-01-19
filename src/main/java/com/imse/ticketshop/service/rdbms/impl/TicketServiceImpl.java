package com.imse.ticketshop.service.rdbms.impl;

import com.imse.ticketshop.entity.rdbms.Ticket;
import com.imse.ticketshop.repository.rdbms.ConcertRepository;
import com.imse.ticketshop.repository.rdbms.CustomerRepository;
import com.imse.ticketshop.repository.rdbms.TicketRepository;
import com.imse.ticketshop.service.rdbms.ConcertService;
import com.imse.ticketshop.service.rdbms.CustomerService;
import com.imse.ticketshop.service.rdbms.TicketService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepo;
    private final CustomerRepository customerRepo;
    private final ConcertRepository concertRepo;
    private final CustomerService customerService;
    private final ConcertService concertService;

    public TicketServiceImpl(TicketRepository ticketRepo,
                             CustomerRepository customerRepo,
                             ConcertRepository concertRepo,
                             CustomerService customerService,
                             ConcertService concertService) {
        this.ticketRepo = ticketRepo;
        this.customerRepo = customerRepo;
        this.concertRepo = concertRepo;
        this.customerService = customerService;
        this.concertService = concertService;
    }


    @Override
    public void generateTickets(List<Map<String, String>> tableData, UUID uuid) {
        Random r = new Random();

        var customer = customerRepo.findById(uuid).get();

        for(var elem : tableData){

            int qty = Integer.parseInt(elem.get("qty"));
            String id = elem.get("id");

            if(qty != 0) {

                var concert = concertService.getConcertByID(id);

                for(int i = 0; i < qty; i++) {
                    var ticket = Ticket.builder()
                            .row(r.nextInt(1, 100))
                            .seat(r.nextInt(1, 100))
                            .customer(customer)
                            .concert(concert)
                            .build();
                    ticketRepo.save(ticket);

                    concert.updateTickets(ticket);
                    concertRepo.save(concert);

                    customer.getTickets().add(ticket);
                    customerRepo.save(customer);
                }


            }
        }
    }
}
