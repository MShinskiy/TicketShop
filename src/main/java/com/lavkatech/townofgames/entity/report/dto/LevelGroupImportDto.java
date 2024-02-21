package com.lavkatech.townofgames.entity.report.dto;

import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import lombok.Getter;

@Getter
public class LevelGroupImportDto extends ImportDto {

    private final Group group;
    private final LevelSA level;
    public LevelGroupImportDto(String dtprf, Group group, LevelSA level) {
        super(dtprf);
        this.group = group;
        this.level = level;
    }
}
