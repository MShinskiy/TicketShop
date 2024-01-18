package com.imse.ticketshop.service.impl;


import com.imse.ticketshop.entity.Concert;
import com.imse.ticketshop.repository.ConcertRepository;
import com.imse.ticketshop.service.ConcertService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConcertServiceImpl implements ConcertService {

    private final ConcertRepository concertRepo;

    public ConcertServiceImpl(ConcertRepository concertRepo) {
        this.concertRepo = concertRepo;
    }

    @Override
    public List<Concert> getAllConcerts() {
        return null;
    }

    @Override
    public List<Concert> getConcertsInCity(String city) {
        return null;
    }

    @Override
    public List<Concert> getConcertsInVenue(String venueName) {
        return null;
    }

    @Override
    public List<Concert> getFilteredConcerts(String city, String venueName) {
        return null;
    }

    @Override
    public void addConcert(Concert concert) {
        concertRepo.save(concert);
    }
}
