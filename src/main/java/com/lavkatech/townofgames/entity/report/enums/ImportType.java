package com.lavkatech.townofgames.entity.report.enums;

public enum ImportType {
    COINS("Начисление монет"),
    TASKS("Значения миссий"),
    LEVEL_GROUP("Резерв значений");

    private final String value;

    public String value() {
        return value;
    }

    ImportType(String value) {
        this.value = value;
    }
}
