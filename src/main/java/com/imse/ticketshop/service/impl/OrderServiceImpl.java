package com.imse.ticketshop.service.impl;

import com.imse.ticketshop.entity.Concert;
import com.imse.ticketshop.entity.Order;
import com.imse.ticketshop.repository.ConcertRepository;
import com.imse.ticketshop.repository.CustomerRepository;
import com.imse.ticketshop.repository.OrderRepository;
import com.imse.ticketshop.service.ConcertService;
import com.imse.ticketshop.service.CustomerService;
import com.imse.ticketshop.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final CustomerRepository customerRepo;
    private final ConcertRepository concertRepo;
    private final CustomerService customerService;
    private final ConcertService concertService;

    public OrderServiceImpl(OrderRepository orderRepo,
                            CustomerRepository customerRepo,
                            ConcertRepository concertRepo,
                            CustomerService customerService,
                            ConcertService concertService) {
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
        this.concertRepo = concertRepo;
        this.customerService = customerService;
        this.concertService = concertService;
    }

    @Override
    public void addNewOrder(List<Map<String, String>> tableData, UUID uuid) {
        LocalDateTime now = LocalDateTime.now();
        var totalPrice = computeTotalPrice(tableData);
        var totalNumberOfTickets = computeTotalNumberOfTickets(tableData);
        var concertsWithTickets =  getConcertsWithTickets(tableData);

        var customer = customerRepo.findById(uuid).get();

        var order = Order.builder()
                .dateIssued(now)
                .price(totalPrice)
                .nTickets(totalNumberOfTickets)
                .concert(concertsWithTickets)
                .customer(customer)
                .build();
        orderRepo.save(order);

        customer.updateOrders(order);
        customerRepo.save(customer);

        for(var concert : concertsWithTickets) {
            concert.updateOrders(order);
            concertRepo.save(concert);
        }
    }

    public double computeTotalPrice(List<Map<String, String>> tableData){

        double price = 0;

        for(var elem : tableData) {

            int qty = Integer.parseInt(elem.get("qty"));
            String id = elem.get("id");
            var concert = concertService.getConcertByID(id);

            price = price + qty * concert.getPrice();
        }
        return price;
    }

    public int computeTotalNumberOfTickets(List<Map<String, String>> tableData){

        int noTickets = 0;

        for(var elem : tableData) {

            int qty = Integer.parseInt(elem.get("qty"));
            String id = elem.get("id");
            var concert = concertService.getConcertByID(id);

            noTickets += qty;
        }
        return noTickets;

    }

    public List<Concert> getConcertsWithTickets(List<Map<String, String>> tableData) {

        var concertsWithTickets = new ArrayList<Concert>();

        for(var elem : tableData) {

            String id = elem.get("id");
            int qty = Integer.parseInt(elem.get("qty"));

            if (qty != 0) {

                var concert = concertService.getConcertByID(id);
                concertsWithTickets.add(concert);
            }
        }
        return concertsWithTickets;
    }
}
