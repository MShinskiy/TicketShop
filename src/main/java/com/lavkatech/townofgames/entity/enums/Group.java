package com.lavkatech.townofgames.entity.enums;

import java.util.Arrays;

/*TODO дать нормальные имена переменным*/
public enum Group {
    SIX(6),
    NINE(9);

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
