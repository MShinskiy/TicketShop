package com.imse.ticketshop.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "customers")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    private String email;
    private String password;
    private int age;
    private String gender;
    private String phone;
    private String city;
    private String country;

}
