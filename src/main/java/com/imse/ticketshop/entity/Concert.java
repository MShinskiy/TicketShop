package com.imse.ticketshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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

    @OneToMany
    private List<Ticket> tickets;

    @ManyToMany // TODO many to many required?
    private List<Order> orders;
}
