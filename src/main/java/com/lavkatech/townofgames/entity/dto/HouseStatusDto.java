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
    private final long maxCoins;
    private final TaskStatus taskStatus;
    private final String taskDescriptionStringMap;
    private final String caption;

    private final List<TaskJson> tasks;
    private final List<ButtonJson> buttons;

    public HouseStatusDto(String name, String description, int tasksComplete, int tasksTotal, TaskStatus taskStatus, long maxCoins, String taskDescriptionStringMap, String caption) {
        this.name = name;
        this.description = description;
        this.tasksComplete = tasksComplete;
        this.tasksTotal = tasksTotal;
        this.maxCoins = maxCoins;
        this.taskStatus = taskStatus;
        this.taskDescriptionStringMap = taskDescriptionStringMap;
        this.caption = caption;
        tasks = new ArrayList<>();
        buttons = new ArrayList<>();
    }

    public void addTask(String description, Boolean isComplete) {
        tasks.add(new TaskJson(description, isComplete));
    }

    public void addButton(String text, String url) {
        if(text != null && !text.isEmpty() && url != null && !url.isEmpty())
            buttons.add(new ButtonJson(text, url));
    }
}

record TaskJson(String description, Boolean isComplete) {

}

record ButtonJson(String text, String url) {

}
