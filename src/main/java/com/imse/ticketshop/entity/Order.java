package com.imse.ticketshop.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "orders")
@Entity
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;

    private LocalDateTime dateIssued;
    private double price;
    private int nTickets;

    @ManyToMany
    private List<Concert> concert = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;

}
