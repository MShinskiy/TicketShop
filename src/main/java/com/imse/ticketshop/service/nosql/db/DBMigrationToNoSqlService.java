package com.imse.ticketshop.service.nosql.db;

import com.imse.ticketshop.entity.nosql.ConcertNoSql;
import com.imse.ticketshop.entity.nosql.CustomerNoSql;
import com.imse.ticketshop.entity.nosql.OrderNoSql;
import com.imse.ticketshop.entity.nosql.TicketNoSql;
import com.imse.ticketshop.entity.nosql.VenueNoSql;
import com.imse.ticketshop.repository.nosql.ConcertNoSqlRepository;
import com.imse.ticketshop.repository.nosql.CustomerNoSqlRepository;
import com.imse.ticketshop.repository.nosql.VenueNoSqlRepository;
import com.imse.ticketshop.repository.rdbms.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DBMigrationToNoSqlService {

    private final CustomerRepository customerRepository;
    private final VenueRepository venueRepository;
    private final ConcertRepository concertRepository;
    private final CustomerNoSqlRepository customerNoSqlRepository;
    private final VenueNoSqlRepository venueNoSqlRepository;
    private final ConcertNoSqlRepository concertNoSqlRepository;
    private static final Logger log = LogManager.getLogger();


    public DBMigrationToNoSqlService(CustomerRepository customerRepository,
                                     VenueRepository venueRepository,
                                     ConcertRepository concertRepository,
                                     CustomerNoSqlRepository customerNoSqlRepository,
                                     VenueNoSqlRepository venueNoSqlRepository,
                                     ConcertNoSqlRepository concertNoSqlRepository) {
        this.customerRepository = customerRepository;
        this.venueRepository = venueRepository;
        this.concertRepository = concertRepository;
        this.customerNoSqlRepository = customerNoSqlRepository;
        this.venueNoSqlRepository = venueNoSqlRepository;
        this.concertNoSqlRepository = concertNoSqlRepository;
    }

    public void migrateToNoSql() {
        migrateVenues();
        migrateConcerts();
        migrateCustomers();
        log.info("Database has been migrated");
    }

    private void migrateVenues() {

        var venues = venueRepository.findAll();

        for (var venue : venues) {

            var nosqlvenue = VenueNoSql.builder()
                    .venueId(venue.getVenueId().toString())
                    .email(venue.getEmail())
                    .password(venue.getPassword())
                    .name(venue.getName())
                    .address(venue.getAddress())
                    .city(venue.getCity())
                    .country(venue.getCountry())
                    .build();
            venueNoSqlRepository.save(nosqlvenue);
        }
    }

    private void migrateConcerts() {

        var concerts = concertRepository.findAll();

        for (var concert : concerts) {

            // tickets
            var tickets = concert.getTickets();
            var nosqltickets = new ArrayList<TicketNoSql>();

            if (tickets != null) {
                for (var ticket : tickets) {

                    var nosqlticket = TicketNoSql.builder()
                            .ticketId(ticket.getTicketId().toString())
                            .customerId(ticket.getCustomer().getCustomerId().toString())
                            .concertId(ticket.getCustomer().getCustomerId().toString())
                            .row(ticket.getRow())
                            .seat(ticket.getSeat())
                            .build();
                    nosqltickets.add(nosqlticket);
                }
            }

            // orders
            var orders = concert.getOrders();
            var nosqlorders = new ArrayList<OrderNoSql>();

            if (orders != null) {
                for (var order : orders) {

                    var nosqlorder = OrderNoSql.builder()
                            .orderId(order.getOrderId().toString())
                            .customerId(order.getCustomer().getCustomerId().toString())
                            .dateIssued(order.getDateIssued())
                            .price(order.getPrice())
                            .nTickets(order.getNTickets())
                            .build();

                    nosqlorders.add(nosqlorder);
                }
            }

            var nosqlconcert = ConcertNoSql.builder()
                    .concertId(concert.getConcertId().toString())
                    .venueId(concert.getVenue().getVenueId().toString())
                    .band(concert.getBand())
                    .genre(concert.getGenre())
                    .startTime(concert.getStartTime())
                    .price(concert.getPrice())
                    .location(concert.getLocation())
                    .capacity(concert.getCapacity())
                    .orders(nosqlorders)
                    .build();
            concertNoSqlRepository.save(nosqlconcert);
        }

    }

    private void migrateCustomers() {

        var customers = customerRepository.findAll();

        for (var customer : customers) {

            var tickets = customer.getTickets();
            var orders = customer.getOrders();
            var nosqltickets = new ArrayList<TicketNoSql>();
            var nosqlorders = new ArrayList<OrderNoSql>();

            if (tickets != null) {

                for (var ticket : tickets) {

                    var nosqlticket = TicketNoSql.builder()
                            .ticketId(ticket.getTicketId().toString())
                            .customerId(ticket.getCustomer().getCustomerId().toString())
                            .concertId(ticket.getCustomer().getCustomerId().toString())
                            .row(ticket.getRow())
                            .seat(ticket.getSeat())
                            .build();
                    nosqltickets.add(nosqlticket);

                    nosqltickets.add(nosqlticket);
                }
            }

            if (orders != null) {

                for (var order : orders) {

                    var nosqlorder = OrderNoSql.builder()
                            .orderId(order.getOrderId().toString())
                            .customerId(order.getCustomer().getCustomerId().toString())
                            .dateIssued(order.getDateIssued())
                            .price(order.getPrice())
                            .nTickets(order.getNTickets())
                            .build();

                    nosqlorders.add(nosqlorder);
                }
            }

            var nosqlcustomer = CustomerNoSql.builder()
                    .customerId(customer.getCustomerId().toString())
                    .email(customer.getEmail())
                    .password(customer.getPassword())
                    .age(customer.getAge())
                    .gender(customer.getGender())
                    .phone(customer.getPhone())
                    .city(customer.getCity())
                    .country(customer.getCountry())
                    .orders(nosqlorders)
                    .tickets(nosqltickets)
                    .build();

            customerNoSqlRepository.save(nosqlcustomer);

        }
    }

}
