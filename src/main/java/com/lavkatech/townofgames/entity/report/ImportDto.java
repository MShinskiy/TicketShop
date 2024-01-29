package com.lavkatech.townofgames.entity.report;

import lombok.Getter;

@Getter
public class ImportDto {

    private final String dtprf;

    public ImportDto(String dtprf) {
        this.dtprf = dtprf;
    }
}
