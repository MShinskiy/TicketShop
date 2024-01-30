package com.lavkatech.townofgames.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class HouseProgress {
    //Task - isComplete
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final Map<UUID, Boolean> tasks;
    private final UUID houseId;
    private final int mapId;
    private long maxCoins = 0;
    private long currentCoins = 0;
    private String descVar1 = "";
    private String descVar2 = "";
    private String descVar3 = "";

    public HouseProgress(UUID houseId, int mapId) {
        this.houseId = houseId;
        this.mapId = mapId;
        tasks = new LinkedHashMap<>();
    }

    // Вернуть кол-во всех заданий
    public int tasksTotal() {
        return tasks.size();
    }

    // Вернуть кол-во завершенных заданий
    public int tasksCompleted() {
        return (int) tasks.values().stream().filter(b -> b).count();
    }

    // Добавить задание
    public void putTask(UUID task) {
        tasks.put(task, false);
    }

    // Вернуть статус задания
    public Boolean getTaskStatus(UUID task) {
        return tasks.get(task);
    }

    // Вернуть задания
    public List<UUID> getTasksList() {
        return new ArrayList<>(tasks.keySet());
    }

    //return false if previous mapping was true (was already marked as finished) or no mapping/null (doesn't exist)
    //true - complete, false - active
    public boolean finishTask(UUID task) {
        Boolean res = tasks.put(task, true);
        return res != null && !res;
    }
}
