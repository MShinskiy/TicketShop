package com.lavkatech.townofgames.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class HouseProgress {
    //Task - isComplete
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final Map<Task, Boolean> tasks;
    private long maxCoins = 0;
    private long currentCoins = 0;
    private String descVar1 = "";
    private String descVar2 = "";
    private String descVar3 = "";

    public HouseProgress() {
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
    public void putTask(Task task) {
        tasks.put(task, false);
    }

    // Вернуть задание
    public Boolean getTaskStatus(Task task) {
        return tasks.get(task);
    }

    // Вернуть задания
    public List<Task> tasksList() {
        return new ArrayList<>(tasks.keySet());
    }

    //return false if previous mapping was true or no mapping (null)
    //true - complete, false - active
    public boolean finishTask(Task task) {
        Boolean res = tasks.put(task, true);
        return res != null && !res;
    }
}
