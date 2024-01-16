package com.lavkatech.townofgames.entity;

import com.lavkatech.townofgames.entity.cosnt.Group;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность описывает игроков
 */
@Data
@Entity
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String dtprf;

    private Group group;
    private long coins = 0;
    private long tickets = 0;
    private long points = 0;
    private LocalDateTime createdOn = LocalDateTime.now();
    private LocalDateTime lastLogin = LocalDateTime.now();
    private LocalDateTime lastCoinChange = LocalDateTime.now();
    private LocalDateTime lastTicketChange = LocalDateTime.now();

    @OneToMany(mappedBy = "user")
    private List<HouseVisitLog> houseVisitLog = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<BalanceLog> balanceLog = new ArrayList<>();
}