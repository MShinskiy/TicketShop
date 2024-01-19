package com.imse.ticketshop.entity.rdbms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DemographicsReport {

    private String genre;
    private String ageRange;
    private String gender;
    private Integer noTickets;


}
