package com.lavkatech.townofgames.service.impl;

import com.lavkatech.townofgames.entity.*;
import com.lavkatech.townofgames.entity.cosnt.TaskStatus;
import com.lavkatech.townofgames.entity.dto.HouseStatusDto;
import com.lavkatech.townofgames.exception.UserNotFoundException;
import com.lavkatech.townofgames.repository.HouseRepository;
import com.lavkatech.townofgames.service.HouseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepo;

    public HouseServiceImpl(HouseRepository houseRepo) {
        this.houseRepo = houseRepo;
    }


    @Override
    public List<HouseStatusDto> getCurrentMapStateForUser(User user) throws UserNotFoundException {

        return null;
    }

    @Override
    public List<HouseStatusDto> getHousesDtosForUser(User user) {
        return houseRepo.findAllByHouseGroup(user.getGroup())
                .stream()
                .map(house -> houseToDto(house, user))
                .collect(Collectors.toList());
    }

    @Override
    public List<House> getAllHouses() {
        return houseRepo.findAll();
    }

    // Получить DTO из статуса дома игрока
    private HouseStatusDto houseToDto(House house, User user) {
        // Получить прогресс пользователя на поле
        UserProgress userProgress =
                UserProgress.fromString(user.getUserProgressJson());

        //Получить прогресс пользователя по дому
        TaskProgress houseProgress = userProgress
                .getProgressPerHouseMap()
                .get(house.getId());

        //Получить данные о заданиях дома
        int tasksCompleted = houseProgress.completed();
        int tasksTotal = houseProgress.total();

        // Всего заданий 0? -> EMPTY, сделаны все задания? -> COMPLETE, иначе AVAILABLE
        TaskStatus status = tasksTotal > 0 ?
                tasksTotal == tasksCompleted ?
                        TaskStatus.COMPLETE : TaskStatus.AVAILABLE
                : TaskStatus.EMPTY;
        // Создание DTO
        HouseStatusDto dto
                = new HouseStatusDto(house.getName(), house.getDescription(), tasksCompleted, tasksTotal, status);

        // Добавление информации о заданиях
        for(Task task : houseProgress.keys())
            dto.addTask(task.getDescription(), houseProgress.get(task));

        // Добавление информации о кнопках
        dto.addButton(house.getButtonText1(), house.getButtonURL1());
        dto.addButton(house.getButtonText2(), house.getButtonURL2());

        return dto;
    }
}
