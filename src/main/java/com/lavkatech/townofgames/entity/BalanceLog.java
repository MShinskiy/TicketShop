package com.lavkatech.townofgames.entity;

import com.lavkatech.townofgames.entity.cosnt.Game;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

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
    @JoinColumn(name = "dtprf")
    private User user;

    private Game game;
    private long spent;
    private long won;
    private LocalDateTime timestamp = LocalDateTime.now();
}
