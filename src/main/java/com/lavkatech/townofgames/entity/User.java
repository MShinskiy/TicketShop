package com.lavkatech.townofgames.entity;

import com.lavkatech.townofgames.entity.dto.UserDto;
import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public User(String dtprf) {
        this.dtprf = dtprf;
    }

    @Id
    private String dtprf = null;

    private String username = null;
    @Enumerated(EnumType.STRING)
    private Group userGroup = null;
    @Enumerated(EnumType.STRING)
    private LevelSA userLevel = null;
    private long coins = 0;
    private long maxCoins = 0;
    private long points = 0;
    private LocalDateTime createdOn = LocalDateTime.now();
    private LocalDateTime lastLogin = LocalDateTime.now();
    private LocalDateTime lastCoinChange = LocalDateTime.now();
    private LocalDateTime lastTicketChange = LocalDateTime.now();
    private String userProgressJson = UserProgress.initString();

/*    @OneToMany(mappedBy = "user")
    private List<UserTask> userTasks;*/

    @OneToMany(mappedBy = "user")
    private List<HouseVisitLog> houseVisitLog = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<BalanceLog> balanceLog = new ArrayList<>();

    public UserDto toDto() {
        UserProgress up = UserProgress.fromString(userProgressJson);
        //Получить сумму сделанных заданий пользователя по зданиям
        int count = up.getProgressPerHouseMap().values().stream().filter(Objects::nonNull)
                .mapToInt(HouseProgress::tasksCompleted).sum();
        //Получить сумму всех заданий пользователя по зданиям
        int total = up.getProgressPerHouseMap().values().stream()
                .mapToInt(HouseProgress::tasksTotal).sum();
        //Получить сумму максимального кол-во монет по зданиям
        long max = up.getProgressPerHouseMap().values().stream()
                .mapToLong(HouseProgress::getMaxCoins).sum();

        return UserDto.builder()
                .dtprf(dtprf)
                .username(username)
                .group(userGroup)
                .level(userLevel)
                .coins(coins)
                .maxCoins(max)
                .points(points)
                .tasksCount(count)
                .tasksTotal(total)
                .build();
    }
}