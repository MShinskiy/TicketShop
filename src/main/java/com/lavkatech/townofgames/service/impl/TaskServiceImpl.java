package com.lavkatech.townofgames.service.impl;

import com.lavkatech.townofgames.entity.House;
import com.lavkatech.townofgames.entity.Task;
import com.lavkatech.townofgames.repository.TaskRepository;
import com.lavkatech.townofgames.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepo;

    public TaskServiceImpl(TaskRepository taskRepo) {
        this.taskRepo = taskRepo;
    }

    @Override
    public Task createTask() {
        return taskRepo.save(new Task());
    }

    @Override
    public Task createTask(int order, String description, House house) {
        return taskRepo.save(new Task(order, description, house));
    }

    @Override
    public Task getTaskByUUID(UUID uuid) {
        return taskRepo.getTaskById(uuid).orElse(null);
    }
}
