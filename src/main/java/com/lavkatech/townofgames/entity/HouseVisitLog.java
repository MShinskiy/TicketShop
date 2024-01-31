package com.lavkatech.townofgames.entity;

import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность логов посещения домов
 */
@Entity
@Table(name = "visits")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseVisitLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime house1;
    private LocalDateTime house2;
    private LocalDateTime house3;
    private LocalDateTime house4;
    private LocalDateTime house5;
    private LocalDateTime house6;
    private LocalDateTime house7;
    private LocalDateTime house8;
    private LocalDateTime house9;

    @ManyToOne
    private User user;
}
