package com.lavkatech.townofgames.entity;

import com.lavkatech.townofgames.entity.enums.Activity;
import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность логов баланса игроков
 */

@Entity
@Table(name = "balances")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BalanceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private Group mapGroup;
    @Enumerated(EnumType.STRING)
    private LevelSA mapLevel;
    @Enumerated(EnumType.STRING)
    private Activity activity;

    private long totalBalance;
    private LocalDateTime timestamp = LocalDateTime.now();
}
