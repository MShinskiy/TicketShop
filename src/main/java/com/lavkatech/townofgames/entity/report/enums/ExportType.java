package com.lavkatech.townofgames.entity.report.enums;

public enum ExportType {
    ACTIVITY("Активность"),
    BALANCE("Начисления");

    private String value;

    ExportType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
