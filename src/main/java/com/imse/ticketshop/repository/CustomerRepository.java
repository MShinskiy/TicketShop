package com.imse.ticketshop.repository;

import com.imse.ticketshop.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    @Override
    Optional<Customer> findById(UUID uuid);
}
