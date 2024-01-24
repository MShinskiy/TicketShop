package com.lavkatech.townofgames.entity.report;

import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.enums.LevelSA;

public record LevelGroupImportDto(String dtprf,
                                  Group group,
                                  LevelSA level) {
}
