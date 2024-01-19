package com.imse.ticketshop.service.rdbms;

import com.imse.ticketshop.entity.rdbms.Venue;
import com.imse.ticketshop.entity.rdbms.dto.GenrePopularityReportDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public interface VenueService {

    List<Venue> getAllVenues();
    Boolean doesVenueWithIdExist(UUID id);
    Venue getVenueById(UUID id);
    List<GenrePopularityReportDto> getReport();
    Set<String> getCities();
}
