package com.imse.ticketshop.repository.nosql;

import com.imse.ticketshop.entity.nosql.OrderNoSql;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderNoSqlRepository extends MongoRepository<OrderNoSql, String> {
}
