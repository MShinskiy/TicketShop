package com.imse.ticketshop.service;

import com.imse.ticketshop.entity.Venue;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface VenueService {

    List<Venue> getAllVenues();
}
