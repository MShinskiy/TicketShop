package com.imse.ticketshop.repository.nosql;

import com.imse.ticketshop.entity.nosql.ConcertNoSql;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConcertNoSqlRepository extends MongoRepository<ConcertNoSql, String> {
}
