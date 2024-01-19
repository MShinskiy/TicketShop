package com.lavkatech.townofgames.entity;

import com.lavkatech.townofgames.entity.enums.Game;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность логов баланса игроков
 */

@Entity
@Table(name = "balances")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BalanceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private Game game;
    private long spent;
    private long won;
    private LocalDateTime timestamp = LocalDateTime.now();
}
