package com.imse.ticketshop.repository;

import com.imse.ticketshop.entity.Venue;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VenueNosqlRepository extends MongoRepository<Venue, String> {



}
