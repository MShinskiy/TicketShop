package com.lavkatech.townofgames.entity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TaskProgress {
    private final Map<Task, Boolean> track;
    public TaskProgress() {
        track = new LinkedHashMap<>();
    }

    // Вернуть кол-во всех заданий
    public int total() {
        return track.size();
    }

    // Вернуть кол-во завершенных заданий
    public int completed() {
        return (int) track.values().stream().filter(b -> b).count();
    }

    // Добавить задание
    public void put(Task task) {
        track.put(task, false);
    }

    // Вернуть задание
    public Boolean get(Task task) {
        return track.get(task);
    }

    // Вернуть задания
    public List<Task> keys() {
        return new ArrayList<>(track.keySet());
    }

    //return false if previous mapping was true or no mapping (null)
    public boolean finish(Task task) {
        Boolean res = track.put(task, true);
        return res != null && !res;
    }
}
