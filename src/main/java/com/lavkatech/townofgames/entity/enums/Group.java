package com.lavkatech.townofgames.entity.enums;

import java.util.Arrays;

public enum Group {
    OTHER(6),
    PARTNER(9);

    final int val;
    private Group(int n){
        this.val = n;
    }

    public static Group groupOf(int value) {
        return Arrays.stream(values())
                .filter(g -> g.val == value)
                .findAny()
                .orElse(null);
    }
}
