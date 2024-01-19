package com.imse.ticketshop.service.db;

import com.imse.ticketshop.entity.*;
import com.imse.ticketshop.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class DBFillerService {

    private static final Logger log = LogManager.getLogger();

    private final String[] countries = new String[]{
            "Austria",
            "Germany",
            "Italy",
            "Ireland",
            "UK"
    };
    private final String[] cities = new String[]{
            "Vienna",
            "Berlin",
            "Rome",
            "Dublin",
            "London",
            "Innsbruck",
            "Munich",
            "Stuttgart",
            "Milano"
    };
    private final String[] domains = new String[]{
            "gmail.com",
            "yahoo.com"
    };
    private final String[] gender = new String[]{
            "male",
            "female"
    };

    private final String[] venueNames = new String[]{
            "Stadthalle",
            "Gasometer",
            "Royal Albert Hall",
            "O2",
            "Zenith",
            "Olympiahalle"
    };

    private final String[] genreNames = new String[]{
            "metal",
            "metalcore",
            "deathcore",
            "alt",
            "pop",
            "punk"
    };

    private final String[] bandNames = new String[]{
            "BMTH",
            "30STM",
            "FIR",
            "MIW",
            "Spiritbox",
            "DWP",
            "Architects"
    };
    private final CustomerRepository customerRepository;
    private final VenueRepository venueRepository;
    private final ConcertRepository concertRepository;
    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;

    public DBFillerService(CustomerRepository customerRepository,
                           VenueRepository venueRepository,
                           ConcertRepository concertRepository,
                           OrderRepository orderRepository,
                           TicketRepository ticketRepository) {

        this.customerRepository = customerRepository;
        this.venueRepository = venueRepository;
        this.concertRepository = concertRepository;
        this.orderRepository = orderRepository;
        this.ticketRepository = ticketRepository;
    }

    public void fillDatabase() {
        fillCustomers();
        fillVenues();
        fillConcerts();
        fillOrders();
        fillTicketsBasedOnOrders();
        log.info("Database has been filled.");
    }

    public void clearDB(){
        ticketRepository.deleteAll();
        orderRepository.deleteAll();
        concertRepository.deleteAll();
        venueRepository.deleteAll();
        customerRepository.deleteAll();
        log.info("Database has been cleared.");
    }

    private String generateRandomString(boolean numbersOnly) {

        String AlphaNumericString;
        if (!numbersOnly) {
            AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "0123456789"
                    + "abcdefghijklmnopqrstuvxyz";
        } else {
            AlphaNumericString = "0123456789";
        }
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            int index
                    = (int) (AlphaNumericString.length()
                    * Math.random());

            sb.append(AlphaNumericString
                    .charAt(index));
        }
        return sb.toString();
    }

    private void fillCustomers() {

        int noCustomers = 10;
        var customers = new Customer[noCustomers];

        Random r = new Random();
        int low = 10;
        int high = 100;

        for (int i = 0; i < noCustomers; i++) {
            customers[i] = Customer.builder()
                    .email(generateRandomString(false) + "@" + domains[i % domains.length])
                    .password(generateRandomString(false))
                    .age(r.nextInt(high - low) + low)
                    .gender(gender[i % gender.length])
                    .phone(generateRandomString(true))
                    .city(cities[i % cities.length])
                    .country(countries[i % countries.length])
                    .build();
        }
        customerRepository.saveAll(List.of(customers));
    }

    private void fillVenues() {

        int noVenues = 10;
        var venues = new Venue[noVenues];
        for (int i = 0; i < noVenues; i++) {
            venues[i] = Venue.builder()
                    .email(generateRandomString(false) + "@" + domains[i % domains.length])
                    .password(generateRandomString(false))
                    .name(venueNames[i % venueNames.length])
                    .address(generateRandomString(false))
                    .city(cities[i % cities.length])
                    .country(countries[i % countries.length])
                    .build();
        }
        venueRepository.saveAll(List.of(venues));
    }

    private void fillConcerts() {

        int noConcerts = 20;
        Random r = new Random();
        LocalDateTime now = LocalDateTime.now();
        var concerts = new Concert[noConcerts];
        var venues = venueRepository.findAll();


        for (int i = 0; i < noConcerts; i++) {
            var venue = venues.get(r.nextInt(venues.size()));
            concerts[i] = Concert.builder()
                    .band(bandNames[i % bandNames.length])
                    .genre(genreNames[i % genreNames.length])
                    .startTime(now.plusYears(r.nextInt(1, 3))
                            .plusMonths(r.nextInt(1, 3))
                            .plusHours(r.nextInt(1, 3)))
                    .price(Math.round(r.nextDouble(1, 150) * 100.0) / 100.0)
                    .location(generateRandomString(false))
                    .capacity(r.nextInt(50, 1000))
                    .venue(venue)
                    .build();

            for (var v : venues) {
                if (v.getVenueId() == venue.getVenueId()) {
                    v.updateConcerts(concerts[i]);
                    venueRepository.save(v);
                }
            }

        }
        concertRepository.saveAll(List.of(concerts));
    }

    private void fillOrders() {

        int noOrders = 20;
        Random r = new Random();
        LocalDateTime now = LocalDateTime.now();
        var orders = new Order[noOrders];
        var concerts = concertRepository.findAll();
        var customers = customerRepository.findAll();

        for (int i = 0; i < noOrders; i++) {

            // for each order pick random customer, pick random concert
            var customer = customers.get(r.nextInt(customers.size()));

            var concert = concerts.get(r.nextInt(concerts.size()));
            var concertList = new ArrayList<Concert>();
            concertList.add(concert);

            // create order
            orders[i] = Order.builder()
                    .dateIssued(now.plusMonths(r.nextInt(1, 3))
                            .plusHours(r.nextInt(1, 3)))
                    .price(concert.getPrice())
                    .nTickets(r.nextInt(1, 10))
                    .concert(concertList)
                    .customer(customer)
                    .build();
            orderRepository.save(orders[i]);

            // update customer
            for(var c : customers) {
                if(c.getCustomerId() == customer.getCustomerId()){
                    c.updateOrders(orders[i]);
                    customerRepository.save(c);
                }
            }

            //update concerts
            for(var c : concerts) {
                if(c.getConcertId() == concert.getConcertId()){
                    c.updateOrders(orders[i]);
                    concertRepository.save(c);
                }
            }
        }
    }

    private void fillTicketsBasedOnOrders() {

        Random r = new Random();
        var orders = orderRepository.findAll();

        for (var order : orders) {

            var customer = order.getCustomer();
            var concert = order.getConcert().get(0);

            for (int i = 0; i < order.getNTickets(); i++) {

                var ticket = Ticket.builder()
                        .row(r.nextInt(1, 100))
                        .seat(r.nextInt(1, 100))
                        .concert(concert)
                        .customer(customer)
                        .build();
                ticketRepository.save(ticket);

                concert.addTicket(ticket);
                concertRepository.save(concert);

                customer.addTicket(ticket);
                customerRepository.save(customer);
            }
        }
    }
}
