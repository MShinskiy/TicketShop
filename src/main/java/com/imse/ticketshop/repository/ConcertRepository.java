package com.imse.ticketshop.repository;

import com.imse.ticketshop.entity.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, UUID> {

    Concert findConcertByConcertId(UUID concertId);

}
