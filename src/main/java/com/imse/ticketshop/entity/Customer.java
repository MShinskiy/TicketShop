package com.imse.ticketshop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID customerId;

    private String email;
    private String password;
    private int age;
    private String gender;
    private String phone;
    private String city;
    private String country;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Ticket> tickets = new ArrayList<>();

    @OneToMany // TODO ??
    private List<Customer> customersGroup = new ArrayList<>();

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
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
}
