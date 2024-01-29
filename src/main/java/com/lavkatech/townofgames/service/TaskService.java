package com.lavkatech.townofgames.service;

import com.lavkatech.townofgames.entity.House;
import com.lavkatech.townofgames.entity.Task;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface TaskService {

    Task createTask();
    Task createTask(String description, House house);

    Task getTaskByUUID(UUID uuid);
}
