package com.imse.ticketshop.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "venues")
@Data
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID venueId;

    private String email;
    private String password;
    private String name;
    private String address;
    private String city;
    private String country;

}
