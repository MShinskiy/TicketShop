package com.lavkatech.townofgames.entity.report;

public enum ImportType {
    COINS("Начисление монет"),
    TASKS("Переменные значения миссий"),
    LEVEL_GROUP("Резерв значений");

    private final String value;

    public String value() {
        return value;
    }

    ImportType(String value) {
        this.value = value;
    }
}
