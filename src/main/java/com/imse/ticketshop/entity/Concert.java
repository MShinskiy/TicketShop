package com.imse.ticketshop.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "concerts")
@Data
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

}
