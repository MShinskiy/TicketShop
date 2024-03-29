package com.imse.ticketshop.service.rdbms.impl;

import com.imse.ticketshop.entity.rdbms.Venue;
import com.imse.ticketshop.entity.rdbms.dto.GenrePopularityReportDto;
import com.imse.ticketshop.repository.rdbms.VenueRepository;
import com.imse.ticketshop.service.rdbms.VenueService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

    @Override
    public Boolean doesVenueWithIdExist(UUID id) {
        return venueRepo.existsById(id);
    }

    @Override
    public Venue getVenueById(UUID id) {
        return venueRepo.findById(id).orElse(null);
    }

    @Override
    public List<GenrePopularityReportDto> getReport() {
        return venueRepo.generateReport().stream()
                .map(row -> GenrePopularityReportDto.builder()
                        .cityVenue(String.valueOf(row[0]))
                        .nameVenue(String.valueOf(row[1]))
                        .bandConcert(String.valueOf(row[2]))
                        .genreConcert(String.valueOf(row[3]))
                        .nTickets(Integer.parseInt(String.valueOf(row[4])))
                        .build())
                .toList();
    }

    @Override
    public Set<String> getCities() {
        var venues = venueRepo.findAll();
        var cities = new HashSet<String>();
        for (var v : venues) {
            cities.add(v.getCity());
        }
        return cities;
    }
}
