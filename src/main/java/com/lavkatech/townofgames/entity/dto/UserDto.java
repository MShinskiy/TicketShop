package com.lavkatech.townofgames.entity.dto;

import com.lavkatech.townofgames.entity.enums.Group;
import lombok.Builder;

@Builder
public record UserDto(String dtprf,
                      String username,
                      long coins,
                      Group group,
                      long points,
                      int tasksCount,
                      int tasksTotal) {
}
