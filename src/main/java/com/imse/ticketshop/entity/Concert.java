package com.imse.ticketshop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "concerts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID concertId;
    private String band;
    private String genre;
    private LocalDateTime startTime;
    private double price;
    private String location;
    private int capacity;

    @ManyToOne
    @JoinColumn(name = "venueId")
    private Venue venue;

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Ticket> tickets;

    @ManyToMany(mappedBy = "concert", cascade = CascadeType.ALL) // TODO many to many required?
    @ToString.Exclude
    private List<Order> orders;

    public void updateOrders(Order o) {
        if(this.orders == null)
            orders = new ArrayList<>();
        this.orders.add(o);
    }

    public void addTicket(Ticket t) {
        if (this.tickets == null)
            tickets = new ArrayList<>();
        this.tickets.add(t);
    }

    public void updateTickets(Ticket t){
        if(this.tickets == null)
            tickets = new ArrayList<>();
        this.tickets.add(t);
    }
}
