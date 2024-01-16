package com.lavkatech.townofgames.entity.dto;

import com.lavkatech.townofgames.entity.cosnt.TaskStatus;
import com.lavkatech.townofgames.entity.cosnt.VisibilityStatus;

/**
 * DTO для передачи данных о домов на карту
 */
public record HouseStatusDto(String name,
                             String description,
                             String taskDescription1,
                             String taskDescription2,
                             String buttonText1,
                             String buttonText2,
                             int tasksComplete,
                             int tasksTotal,
                             TaskStatus taskStatus,
                             VisibilityStatus visibilityStatus
) {}
