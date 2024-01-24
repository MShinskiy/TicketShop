package com.lavkatech.townofgames.entity.dto;

import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import lombok.Builder;

@Builder
public record UserDto(String dtprf,
                      String username,
                      long coins,
                      long maxCoins,
                      Group group,
                      LevelSA level,
                      long points,
                      int tasksCount,
                      int tasksTotal) {
}
