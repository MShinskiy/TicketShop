package com.imse.ticketshop.entity.nosql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "customers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerNoSql {

    @Id
    private String customerId;

    private String email;
    private String password;
    private int age;
    private String gender;
    private String phone;
    private String city;
    private String country;

    private List<TicketNoSql> tickets;

    private List<OrderNoSql> orders;

}