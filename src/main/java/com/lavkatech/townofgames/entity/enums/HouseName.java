package com.lavkatech.townofgames.entity.enums;

import java.util.Arrays;
import java.util.Objects;

public enum HouseName {
    UPGRADE("Салон Апгрейд"),
    UNI("Университет"),
    CHECKS("Дом чеков"),
    Q_CLUB("Q Клуб"),
    CHIPS("Фабрика фишек"),
    ACCESSORIES("Магазин Аксессуаров"),
    PHOTO("Фото студия"),
    RENTING("Прокат инвентаря"),
    GAME_CLUB("Игровой клуб");

    private String name;

    HouseName(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static HouseName houseOf(String value) {
        return Arrays.stream(values())
                .filter(h -> Objects.equals(h.name, value))
                .findAny()
                .orElse(null);
    }
}
