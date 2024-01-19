package com.imse.ticketshop.entity.nosql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "tickets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketNoSql {

    @Id
    private String ticketId;
    private String customerId;
    private String concertId;

    private int row;
    private int seat;

}
