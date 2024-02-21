package com.lavkatech.townofgames.entity.report.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class BalanceLogExportDto extends ExportDto{

    private final UUID id;
    private final String group;
    private final String level;
    private final String activity;
    private final int balance;
    private final String timestamp;

    public BalanceLogExportDto(String dtprf, UUID id, String group, String level, String activity, int balance, String timestamp) {
        super(dtprf);
        this.id = id;
        this.group = group;
        this.level = level;
        this.activity = activity;
        this.balance = balance;
        this.timestamp = timestamp;
    }
}
