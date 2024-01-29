package com.lavkatech.townofgames.service.impl;

import com.lavkatech.townofgames.entity.*;
import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.dto.HouseStatusDto;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import com.lavkatech.townofgames.entity.enums.TaskStatus;
import com.lavkatech.townofgames.exception.UserNotFoundException;
import com.lavkatech.townofgames.repository.HouseRepository;
import com.lavkatech.townofgames.service.HouseService;
import com.lavkatech.townofgames.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepo;
    private final TaskService taskService;

    public HouseServiceImpl(HouseRepository houseRepo, TaskService taskService) {
        this.houseRepo = houseRepo;
        this.taskService = taskService;
    }


    @Override
    public List<HouseStatusDto> getCurrentMapStateForUser(User user) throws UserNotFoundException {
        return null;
    }

    @Override
    public List<HouseStatusDto> getHousesDtosForUser(User user) {
        final Group userGroup = user.getUserGroup();
        final LevelSA userLevel = user.getUserLevel();
        return houseRepo.findAll()
                .stream()
                // Отфильтровать дома соответственно группе
                .filter(house -> userGroup == house.getHouseGroup() && userLevel == house.getHouseLevel())
                // Запаковать в dto
                .map(house -> houseToDto(user, house))
                .collect(Collectors.toList());
    }

    // Получить DTO из статуса дома игрока
    private HouseStatusDto houseToDto(User user, House house) {
        // Получить прогресс пользователя на поле
        UserProgress userProgress =
                UserProgress.fromString(user.getUserProgressJson());

        //Получить прогресс пользователя по дому
        HouseProgress houseProgress = userProgress
                .getProgressPerHouseMap()
                .get(house.getId());

        //Получить данные о заданиях дома
        int tasksCompleted = houseProgress.tasksCompleted();
        int tasksTotal = houseProgress.tasksTotal();
        String renderedString = String.format(house.getTaskProgressDescription(), new String[]{
                houseProgress.getDescVar1(),
                houseProgress.getDescVar2(),
                houseProgress.getDescVar3(),
        });
        long maxCoins = houseProgress.getMaxCoins();

        // Всего заданий 0? -> EMPTY, сделаны все задания? -> COMPLETE, иначе AVAILABLE
        TaskStatus status = tasksTotal > 0 ?
                tasksTotal == tasksCompleted ?
                        TaskStatus.COMPLETE : TaskStatus.AVAILABLE
                : TaskStatus.EMPTY;
        // Создание DTO
        HouseStatusDto dto
                = new HouseStatusDto(house.getName(), house.getDescription(), tasksCompleted, tasksTotal, status, maxCoins, renderedString, house.getCaption());

        // Добавление информации о заданиях
        for(UUID taskId : houseProgress.getTasksList()) {
            Task task = taskService.getTaskByUUID(taskId);
            dto.addTask(task.getDescription(), houseProgress.getTaskStatus(taskId));
        }

        // Добавление информации о кнопках
        dto.addButton(house.getButtonText1(), house.getButtonURL1());
        dto.addButton(house.getButtonText2(), house.getButtonURL2());
        dto.addButton(house.getButtonText3(), house.getButtonURL3());

        return dto;
    }

    @Override
    public List<House> getAllHouses() {
        return houseRepo.findAll();
    }

    @Override
    public House createHouse(int mapId, Group group, LevelSA level) {
        return houseRepo.save(
                House.builder()
                        .mapId(mapId)
                        .houseGroup(group)
                        .houseLevel(level)
                        .build());
    }

    @Override
    public House createHouse(int mapId, String name, Group group, LevelSA level) {
        return houseRepo.save(new House(mapId, name, group, level));
    }

    @Override
    public List<House> getHousesForGroupAndLevel(Group group, LevelSA level) {
        return houseRepo.findHouseByHouseGroupAndHouseLevel(group, level);
    }
}
