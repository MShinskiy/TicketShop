package com.lavkatech.townofgames.entity.enums;

import java.util.Arrays;
import java.util.Objects;

public enum LevelSA {
    LOW("Низкий"),
    HIGH("Высокий");

    private String name;

    LevelSA(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static LevelSA levelOf(String value) {
        return Arrays.stream(values())
                .filter(l -> Objects.equals(l.name, value))
                .findAny()
                .orElse(null);
    }
}
