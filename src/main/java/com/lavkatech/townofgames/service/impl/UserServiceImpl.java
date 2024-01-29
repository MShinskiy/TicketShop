package com.lavkatech.townofgames.service.impl;

import com.lavkatech.townofgames.entity.*;
import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import com.lavkatech.townofgames.entity.report.CoinImportDto;
import com.lavkatech.townofgames.entity.report.ImportDto;
import com.lavkatech.townofgames.entity.report.LevelGroupImportDto;
import com.lavkatech.townofgames.entity.report.TasksImportDto;
import com.lavkatech.townofgames.repository.UserRepository;
import com.lavkatech.townofgames.service.HouseService;
import com.lavkatech.townofgames.service.TaskService;
import com.lavkatech.townofgames.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LogManager.getLogger();
    private final UserRepository userRepo;
    private final TaskService taskService;
    private final HouseService houseService;

    public UserServiceImpl(UserRepository userRepo, TaskService taskService, HouseService houseService) {
        this.userRepo = userRepo;
        this.taskService = taskService;
        this.houseService = houseService;
    }

    @Override
    public User getUserOrNull(String dtprf) {
        return userRepo.findUserByDtprf(dtprf).orElse(null);
    }

    @Override
    public void updateUsers(List<? extends ImportDto> importLines) {
        if(importLines == null || importLines.isEmpty()) {
            log.error("No importing lines or received list is null.");
            return;
        }
        int count = 0;
        for (ImportDto line : importLines) {
            //Получить пользователя для которого происходит импорт
            User user = userRepo
                    .findUserByDtprf(line.getDtprf())
                    .orElse(null/*createUser(line.getDtprf())*/);
            //Импорт
            if(line instanceof CoinImportDto dto ) {
                //Начисление монет.
                //Получить статус домов
                UserProgress userProgress = UserProgress.fromString(user.getUserProgressJson());
                HouseProgress houseProgress = userProgress.getHouseProgressByHouseMapId(dto.getHouseMapId());
                //Дом не найден
                if(houseProgress == null) {
                    log.error("House to update is not found for user.");
                    break;
                }
                //Внести изменения
                houseProgress.setCurrentCoins(dto.getNewValue());
                houseProgress.setMaxCoins(dto.getMaxValue());
                //Внести изменения в пользователя для общих чисел
                user.setCoins(userProgress.getTotalCurrentCoinsForHouses());
                user.setMaxCoins(userProgress.getTotalMaxCoinsForHouses());
                //Сохранить статус домов
                user.setUserProgressJson(userProgress.toString());
                //Сохранить пользователя
                userRepo.save(user);
                //Посчитать кол-во обработанных строк
                count++;
            } else if(line instanceof TasksImportDto dto) {
                //Переменные значения миссий.
                //Получить статус домов
                UserProgress userProgress = UserProgress.fromString(user.getUserProgressJson());
                HouseProgress houseProgress = userProgress.getHouseProgressByHouseMapId(dto.getHouseMapId());
                //Дом не найден
                if(houseProgress == null) {
                    log.error("House to update is not found for user.");
                    break;
                }
                //Обновить переменные
                houseProgress.setDescVar1(dto.getVar1());
                houseProgress.setDescVar2(dto.getVar2());
                houseProgress.setDescVar3(dto.getVar3());
                //Отметить завершенные задания
                if(dto.isTaskComplete()) {
                    List<UUID> tasks = houseProgress.getTasksList();

                    if (dto.getTaskCode() > tasks.size()) {
                        UUID taskId = tasks.get(dto.getTaskCode() - 1);
                        houseProgress.finishTask(taskId);
                    } else
                        log.error("Task to update is not found for user and house.");
                }
                //Сохранить статус домов
                user.setUserProgressJson(userProgress.toString());
                //Сохранить пользователя
                userRepo.save(user);
                //Посчитать кол-во обработанных строк
                count++;
            } else if(line instanceof LevelGroupImportDto dto) {
                //Резервные значения группы и уровня.
                //Заменить группу и уровень
                user.setUserGroup(dto.getGroup());
                user.setUserLevel(dto.getLevel());
                //Сохранить пользователя
                userRepo.save(user);
            } else
                log.error("Unknown class for line {}", line);
        }
        log.info("{} lines processed, {} lines updated", importLines.size(), count);
    }

    @Override
    public User createUser(String dtprf, String username, @NonNull Group group, @NonNull LevelSA level) {
        List<House> housesForUser = houseService.getHousesForGroupAndLevel(group, level);
        return userRepo.save(
                User.builder()
                        .dtprf(dtprf)
                        .username(username)
                        .userProgressJson(UserProgress.initString(housesForUser))
                        .userGroup(group)
                        .userLevel(level)
                        .createdOn(LocalDateTime.now())
                        .build());
    }

    @Override
    public void addVisitLog(User user, HouseVisitLog log) {
        user.getHouseVisitLog().add(log);
        userRepo.save(user);
    }

    @Override
    public void addBalanceLog(User user, BalanceLog log) {
        user.getBalanceLog().add(log);
        userRepo.save(user);
    }
}
