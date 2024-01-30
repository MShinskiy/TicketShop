package com.lavkatech.townofgames.service.impl;

import com.lavkatech.townofgames.entity.*;
import com.lavkatech.townofgames.entity.dto.HouseEdit;
import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.dto.HouseStatusDto;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import com.lavkatech.townofgames.entity.enums.TaskStatus;
import com.lavkatech.townofgames.exception.UserNotFoundException;
import com.lavkatech.townofgames.repository.HouseRepository;
import com.lavkatech.townofgames.service.HouseService;
import com.lavkatech.townofgames.service.TaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HouseServiceImpl implements HouseService {

    private final HouseRepository houseRepo;
    private final TaskService taskService;
    private static final Logger log = LogManager.getLogger();

    public HouseServiceImpl(HouseRepository houseRepo, TaskService taskService) {
        this.houseRepo = houseRepo;
        this.taskService = taskService;
    }


    @Override
    public List<HouseStatusDto> getCurrentMapStateForUser(User user) throws UserNotFoundException {
        return null;
    }

    @Override
    public Map<Integer, HouseStatusDto> getHousesDtosForUserWithGroupAndLevel(User user, Group group, LevelSA level) {
        return houseRepo.findAll()
                .stream()
                // Отфильтровать дома соответственно группе
                .filter(house -> group == house.getHouseGroup() && level == house.getHouseLevel())
                // Запаковать в dto
                .collect(Collectors.toMap(House::getMapId, house -> houseToDto(user, house)));
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
    public House getHouseForGroupLevelMapId(Group group, LevelSA level, Integer mapId) {
        return houseRepo.findHouseByHouseGroupAndHouseLevelAndMapId(group, level, mapId)
                .orElse(null);
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

    @Override
    public void applyChanges(List<HouseEdit> edits) throws IllegalStateException{
        List<House> houses = getAllHouses();
        for (HouseEdit e : edits) {
            //get
            House house = houses.stream()
                    .filter( h ->
                            h.getHouseGroup() == Group.valueOf(e.getGroup()) &&
                                    h.getHouseLevel() == LevelSA.valueOf(e.getLevel()) &&
                                    h.getMapId() == Integer.parseInt(e.getMapId())
                    )
                    .findAny().orElse(null);

            //validate
            if(house == null) {
                log.error("Error getting house with level {}, group {} and mapId {}", e.getLevel(), e.getGroup(), e.getMapId());
                throw new IllegalStateException(String.format("Error getting house with level %s, group %s and mapId %s", e.getLevel(), e.getGroup(), e.getMapId()));
            }

            //update
            house.setName(e.getName());
            house.setDescription(e.getDescription());
            house.setButtonText1(e.getText1());
            house.setButtonURL1(e.getUrl1());
            house.setButtonText2(e.getText2());
            house.setButtonURL2(e.getUrl2());
            house.setButtonText3(e.getText3());
            house.setButtonURL3(e.getUrl3());
            house.setTaskProgressDescription(e.getProgress());
            house.setCaption(e.getCaption());

            if(e.getTask1() != null && !e.getTask1().isEmpty()) {
                Task task = taskService.createTask(e.getTask1(), house);
                house.setTask1(task);
            }
            if(e.getTask2() != null && !e.getTask2().isEmpty()) {
                Task task = taskService.createTask(e.getTask2(), house);
                house.setTask2(task);
            }

            //save
            houseRepo.save(house);
        }
    }
}
