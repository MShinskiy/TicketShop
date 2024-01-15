package com.imse.ticketshop.service;

import com.imse.ticketshop.entity.Concert;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ConcertService {

    List<Concert> getAllConcerts();
    List<Concert> getConcertsInCity(String city);
    List<Concert> getConcertsInVenue(String venueName);
    List<Concert> getFilteredConcerts(String city, String venueName);
}
