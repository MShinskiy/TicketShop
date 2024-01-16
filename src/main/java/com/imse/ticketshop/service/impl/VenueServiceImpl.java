package com.imse.ticketshop.service.impl;

import com.imse.ticketshop.entity.Venue;
import com.imse.ticketshop.repository.VenueRepository;
import com.imse.ticketshop.service.VenueService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VenueServiceImpl implements VenueService {

    private final VenueRepository venueRepo;

    public VenueServiceImpl(VenueRepository venueRepo) {
        this.venueRepo = venueRepo;
    }

    @Override
    public List<Venue> getAllVenues() {
        return venueRepo.findAll();
    }
}
