package com.imse.ticketshop.entity.rdbms;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name = "orders")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;

    private LocalDateTime dateIssued;
    private double price;
    private int nTickets;

    @ManyToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Concert> concert = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;

}
