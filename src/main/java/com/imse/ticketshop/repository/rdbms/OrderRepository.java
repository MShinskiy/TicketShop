package com.imse.ticketshop.repository.rdbms;

import com.imse.ticketshop.entity.rdbms.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
