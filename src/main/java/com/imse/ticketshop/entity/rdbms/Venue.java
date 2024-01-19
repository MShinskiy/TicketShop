package com.imse.ticketshop.entity.rdbms;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Concert> concerts = new ArrayList<>();

    public void updateConcerts(Concert c){
        if(this.concerts == null)
            concerts = new ArrayList<>();
        this.concerts.add(c);
    }
}
