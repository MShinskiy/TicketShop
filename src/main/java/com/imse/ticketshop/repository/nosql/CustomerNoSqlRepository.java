package com.imse.ticketshop.repository.nosql;

import com.imse.ticketshop.entity.nosql.CustomerNoSql;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface CustomerNoSqlRepository extends MongoRepository<CustomerNoSql, String> {
}
