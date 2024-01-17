package com.lavkatech.townofgames.entity.dto;

import com.lavkatech.townofgames.entity.cosnt.TaskStatus;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO для передачи данных о домов на карту
 */
@Builder
public class HouseStatusDto {
    private final String name;
    private final String description;

    private final int tasksComplete;
    private final int tasksTotal;
    private final TaskStatus taskStatus;
    private final List<TaskDto> tasks;
    private final List<ButtonDto> buttons;

    public HouseStatusDto(String name, String description, int tasksComplete, int tasksTotal, TaskStatus taskStatus) {
        this.name = name;
        this.description = description;
        this.tasksComplete = tasksComplete;
        this.tasksTotal = tasksTotal;
        this.taskStatus = taskStatus;
        tasks = new ArrayList<>();
        buttons = new ArrayList<>();
    }


    public void addTask(String description, Boolean isComplete) {
        tasks.add(new TaskDto(description, isComplete));
    }

    public void addButton(String text, String url) {
        buttons.add(new ButtonDto(text, url));
    }
}

record TaskDto(String description, Boolean isComplete) {

}

record ButtonDto(String text, String url) {

}
