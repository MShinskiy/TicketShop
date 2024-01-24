package com.lavkatech.townofgames.entity.report;

public enum ImportType {
    COINS("Начисление монет"),
    TASKS("Переменные значения миссий"),
    LEVEL_GROUP("Резерв значений");

    private final String value;


    ImportType(String value) {
        this.value = value;
    }
}
