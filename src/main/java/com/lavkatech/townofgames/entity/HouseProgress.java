package com.lavkatech.townofgames.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

import static com.lavkatech.townofgames.misc.Util.renderString;

@Getter
@Setter
public class HouseProgress {

    //private final UUID houseId;
    private final int mapId;
    private long maxCoins = 0;
    private long currentCoins = 0;
    private String descVar1 = " ";
    private String descVar2 = " ";
    private String descVar3 = " ";
    private String taskDesc1 = "";
    private boolean taskStatus1 = false;
    private String taskDesc2 = "";
    private boolean taskStatus2 = false;
    private String visitTimeStamp = "";

    public HouseProgress(int mapId) {
        this.mapId = mapId;
    }

    // Вернуть кол-во всех заданий
    public int tasksTotal() {
        int count = 0;
        if(!taskDesc1.isEmpty() && !renderString(taskDesc1, descVar1, descVar2, descVar3).equalsIgnoreCase("недоступно"))
            count++;
        if(!taskDesc2.isEmpty()  && !renderString(taskDesc2, descVar1, descVar2, descVar3).equalsIgnoreCase("недоступно"))
            count++;
        return count;
    }

    // Вернуть кол-во завершенных заданий
    public int tasksCompleted() {
        int count = 0;
        if(taskStatus1) count++;
        if(taskStatus2) count++;
        return count;
    }
}
