package com.lavkatech.townofgames.entity.report;

import com.lavkatech.townofgames.entity.enums.Group;

public record ImportDto(String dtprf,
                        int coins,
                        int points,
                        int tasks,
                        Group group) {
}
