package com.imse.ticketshop.service.rdbms;

import com.imse.ticketshop.entity.rdbms.Concert;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ConcertService {
    Concert getConcertByID(String id);
    List<Concert> getConcertsByCity(String city);
    void addConcert(Concert concert);
}
