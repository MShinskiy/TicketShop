package com.imse.ticketshop.entity.rdbms;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID ticketId;

    private int row;
    private int seat;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="customerId")
    private Customer customer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "concertId")
    private Concert concert;
}
