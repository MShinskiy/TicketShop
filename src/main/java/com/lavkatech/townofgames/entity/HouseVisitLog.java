package com.lavkatech.townofgames.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность логов посещения домов
 */
@Entity
@Table(name = "visits")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseVisitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDateTime timestamp;


    @ManyToOne
    private User user;
}
