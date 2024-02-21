package com.lavkatech.townofgames.entity.report.dto;

import lombok.Getter;

@Getter
public class ExportDto {

    private final String dtprf;

    public ExportDto(String dtprf) {
        this.dtprf = dtprf;
    }
}
