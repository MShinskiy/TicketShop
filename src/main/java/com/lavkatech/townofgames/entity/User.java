package com.lavkatech.townofgames.entity;

import com.lavkatech.townofgames.entity.dto.UserDto;
import com.lavkatech.townofgames.entity.enums.Group;
import jakarta.persistence.*;
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

    private String username;
    @Enumerated(EnumType.STRING)
    private Group userGroup;
    private long coins = 0;
    private long points = 0;
    private LocalDateTime createdOn = LocalDateTime.now();
    private LocalDateTime lastLogin = LocalDateTime.now();
    private LocalDateTime lastCoinChange = LocalDateTime.now();
    private LocalDateTime lastTicketChange = LocalDateTime.now();
    private String userProgressJson;

/*    @OneToMany(mappedBy = "user")
    private List<UserTask> userTasks;*/

    @OneToMany(mappedBy = "user")
    private List<HouseVisitLog> houseVisitLog = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<BalanceLog> balanceLog = new ArrayList<>();

    public UserDto toDto() {
        return UserDto.builder()
                .dtprf(dtprf)
                .username(username)
                .group(userGroup)
                .coins(coins)
                .points(points)
                .build();
    }
}