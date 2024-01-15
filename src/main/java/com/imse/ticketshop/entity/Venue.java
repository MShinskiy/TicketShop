package com.imse.ticketshop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "venues")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToMany(mappedBy = "venue")
    private List<Concert> concerts;
}
