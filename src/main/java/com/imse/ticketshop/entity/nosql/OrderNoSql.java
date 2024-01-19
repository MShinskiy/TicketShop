package com.imse.ticketshop.entity.nosql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "orders")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderNoSql {

    @Id
    private String orderId;
    private String customerId;
    private LocalDateTime dateIssued;
    private double price;
    private int nTickets;

}
