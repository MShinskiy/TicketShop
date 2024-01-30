package com.lavkatech.townofgames.entity.enums;

import java.util.Arrays;
import java.util.Objects;

@Deprecated
public enum HouseName {
    UPGRADE("Салон Апгрейд", 1),
    UNI("Университет", 2),
    CHECKS("Дом чеков", 3),
    Q_CLUB("Q Клуб", 4),
    CHIPS("Фабрика фишек", 5),
    PHOTO("Фото студия", 6),
    ACCESSORIES("Магазин Аксессуаров", 7),
    RENTING("Прокат инвентаря", 8),
    GAME_CLUB("Игровой клуб", 9);

    private String canonicalName;
    private final int id;

    HouseName(String name, int id) {
        this.id = id;
        this.canonicalName = name;
    }

    public void name(String name) {
        this.canonicalName = name;
    }

    public int id() {
        return id;
    }

    public String canonicalName() {
        return canonicalName;
    }

    public static HouseName houseOf(String value) {
        return Arrays.stream(values())
                .filter(h -> Objects.equals(h.canonicalName, value))
                .findAny()
                .orElse(null);
    }
}
