package com.imse.ticketshop.repository.nosql;

import com.imse.ticketshop.entity.nosql.VenueNoSql;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VenueNoSqlRepository extends MongoRepository<VenueNoSql, String> {
}
