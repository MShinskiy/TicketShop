package com.lavkatech.townofgames.entity.report.dto;

import lombok.Getter;

@Getter
public class ImportDto {

    private final String dtprf;

    public ImportDto(String dtprf) {
        this.dtprf = dtprf;
    }
}
