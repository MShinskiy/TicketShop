package com.lavkatech.townofgames.service.impl;

//import com.lavkatech.townofgames.entity.Task;
//import com.lavkatech.townofgames.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl {

    /*private final TaskRepository taskRepo;

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

    @Override
    public int getOrderByUUID(UUID uuid) throws NoSuchElementException {
        return taskRepo.getTaskById(uuid).orElseThrow().getTaskOrder();
    }

    @Override
    public void deleteTaskByUUID(UUID uuid) {
        taskRepo.deleteById(uuid);
    }*/
}
