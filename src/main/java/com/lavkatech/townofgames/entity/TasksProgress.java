package com.lavkatech.townofgames.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TasksProgress {
    private final Map<Task, Boolean> tasks;
    public TasksProgress() {
        tasks = new LinkedHashMap<>();
    }

    // Вернуть кол-во всех заданий
    public int total() {
        return tasks.size();
    }

    // Вернуть кол-во завершенных заданий
    public int completed() {
        return (int) tasks.values().stream().filter(b -> b).count();
    }

    // Добавить задание
    public void put(Task task) {
        tasks.put(task, false);
    }

    // Вернуть задание
    public Boolean get(Task task) {
        return tasks.get(task);
    }

    // Вернуть задания
    public List<Task> keys() {
        return new ArrayList<>(tasks.keySet());
    }

    //return false if previous mapping was true or no mapping (null)
    public boolean finish(Task task) {
        Boolean res = tasks.put(task, true);
        return res != null && !res;
    }
}
