package com.imse.ticketshop.repository;

import com.imse.ticketshop.entity.Venue;
import com.imse.ticketshop.entity.dto.GenrePopularityReportDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface VenueRepository extends JpaRepository<Venue, UUID> {

    @Query(nativeQuery = true, value =
        """
        SELECT v.city, v.name, c.band, c.genre, COUNT(t) n_tickets FROM venues v\s
        JOIN public.concerts c on v.venue_id = c.venue_id\s
        JOIN public.tickets t on c.concert_id = t.concert_id\s
        GROUP BY v.city, v.name, c.band, c.genre\s
        ORDER BY n_tickets DESC;
        """)
    List<Object[]> generateReport();
}
