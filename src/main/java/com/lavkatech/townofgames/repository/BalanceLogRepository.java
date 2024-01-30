package com.lavkatech.townofgames.repository;

import com.lavkatech.townofgames.entity.BalanceLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BalanceLogRepository extends JpaRepository<BalanceLog, UUID> {
}