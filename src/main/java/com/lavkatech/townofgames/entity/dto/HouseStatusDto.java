package com.lavkatech.townofgames.entity.dto;

import com.lavkatech.townofgames.entity.enums.TaskStatus;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO для передачи данных о домах на карту
 */
@Data
public class HouseStatusDto {

    private final String name;
    private final String description;
    private int tasksComplete;
    private int tasksTotal;
    private long maxCoins;
    private TaskStatus taskStatus;
    private String taskDescriptionStringMap;
    private final String caption;

    private final List<TaskJson> tasks;
    private final List<ButtonJson> buttons;

    public HouseStatusDto(String name, String description, String caption) {
        this.name = name;
        this.description = description;
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
