package com.imse.ticketshop.repository;

import com.imse.ticketshop.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    @Query(nativeQuery = true, value = "SELECT c.genre, \n" +
            "       CASE\n" +
            "           WHEN age >= 16 AND age <= 19 THEN '16-19'\n" +
            "           WHEN age >= 20 AND age <= 25 THEN '20-25'\n" +
            "           WHEN age >= 26 AND age <= 32 THEN '26-32'\n" +
            "           WHEN age >= 33 AND age <= 40 THEN '33-40'\n" +
            "           WHEN age >= 41 AND age <= 50 THEN '40-50'\n" +
            "           ELSE '50+'\n" +
            "       END AS age_group,\n" +
            "       gender,\n" +
            "       COUNT(*) AS ticket_count\n" +
            "FROM Customers cu\n" +
            "JOIN Tickets t ON cu.customer_id = t.customer_id\n" +
            "JOIN Concerts c ON t.concert_id = c.concert_id\n" +
            "GROUP BY c.genre, age_group, gender\n" +
            "ORDER BY c.genre, age_group, gender;")
    List<Object[]> demographicsReportData();
}
