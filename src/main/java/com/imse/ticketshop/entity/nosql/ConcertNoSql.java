package com.imse.ticketshop.entity.nosql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "concerts")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertNoSql {

    @Id
    private String concertId;
    private String venueId;

    private String band;
    private String genre;
    private LocalDateTime startTime;
    private double price;
    private String location;
    private int capacity;

    private List<TicketNoSql> tickets;

    private List<OrderNoSql> orders;
}
