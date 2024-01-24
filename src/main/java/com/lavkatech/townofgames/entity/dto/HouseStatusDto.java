package com.lavkatech.townofgames.entity.dto;

import com.lavkatech.townofgames.entity.enums.TaskStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO для передачи данных о домов на карту
 */
@Data
public class HouseStatusDto {

    private final String name;
    private final String description;
    private final int tasksComplete;
    private final int tasksTotal;
    private final long coins;
    private final long maxCoins;
    private final TaskStatus taskStatus;
    private final String taskDescriptionStringMap;

    private final List<TaskJson> tasks;
    private final List<ButtonJson> buttons;

    public HouseStatusDto(String name, String description, int tasksComplete, int tasksTotal, TaskStatus taskStatus, long coins, long maxCoins, String taskDescriptionStringMap) {
        this.name = name;
        this.description = description;
        this.tasksComplete = tasksComplete;
        this.tasksTotal = tasksTotal;
        this.coins = coins;
        this.maxCoins = maxCoins;
        this.taskStatus = taskStatus;
        this.taskDescriptionStringMap = taskDescriptionStringMap;
        tasks = new ArrayList<>();
        buttons = new ArrayList<>();
    }

    public void addTask(String description, Boolean isComplete) {
        tasks.add(new TaskJson(description, isComplete));
    }

    public void addButton(String text, String url) {
        buttons.add(new ButtonJson(text, url));
    }
}

record TaskJson(String description, Boolean isComplete) {

}

record ButtonJson(String text, String url) {

}
