package com.imse.ticketshop.entity.nosql;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "venues")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueNoSql {

    @Id
    private String venueId;

    private String email;
    private String password;
    private String name;
    private String address;
    private String city;
    private String country;

}
