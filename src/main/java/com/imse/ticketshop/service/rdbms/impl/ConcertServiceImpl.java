package com.imse.ticketshop.service.rdbms.impl;


import com.imse.ticketshop.entity.rdbms.Concert;
import com.imse.ticketshop.repository.rdbms.ConcertRepository;
import com.imse.ticketshop.service.rdbms.ConcertService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ConcertServiceImpl implements ConcertService {

    private final ConcertRepository concertRepo;

    public ConcertServiceImpl(ConcertRepository concertRepo) {
        this.concertRepo = concertRepo;
    }

    @Override
    public Concert getConcertByID(String id) {
        var concertId = UUID.fromString(id);
        return concertRepo.findById(concertId).get();
    }

    @Override
    public List<Concert> getConcertsByCity(String city) {

        var result = new ArrayList<Concert>();
        var concerts = concertRepo.findAll();
        for(var c : concerts) {
            if(c.getVenue().getCity().equals(city)) {
                result.add(c);
            }
        }
        return result;
    }

    @Override
    public void addConcert(Concert concert) {
        concertRepo.save(concert);
    }
}
