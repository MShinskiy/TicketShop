package com.imse.ticketshop.repository.nosql;

import com.imse.ticketshop.entity.nosql.TicketNoSql;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketNoSqlRepository extends MongoRepository<TicketNoSql, String> {
}
