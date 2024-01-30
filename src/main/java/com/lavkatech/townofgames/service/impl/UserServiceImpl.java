package com.lavkatech.townofgames.service.impl;

import com.lavkatech.townofgames.entity.*;
import com.lavkatech.townofgames.entity.enums.Activity;
import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import com.lavkatech.townofgames.entity.report.CoinImportDto;
import com.lavkatech.townofgames.entity.report.ImportDto;
import com.lavkatech.townofgames.entity.report.LevelGroupImportDto;
import com.lavkatech.townofgames.entity.report.TasksImportDto;
import com.lavkatech.townofgames.repository.UserRepository;
import com.lavkatech.townofgames.service.BalanceLogService;
import com.lavkatech.townofgames.service.HouseService;
import com.lavkatech.townofgames.service.TaskService;
import com.lavkatech.townofgames.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LogManager.getLogger();
    private final UserRepository userRepo;
    private final TaskService taskService;
    private final BalanceLogService balanceLogService;
    private final HouseService houseService;

    public UserServiceImpl(UserRepository userRepo, TaskService taskService, BalanceLogService balanceLogService, HouseService houseService) {
        this.userRepo = userRepo;
        this.taskService = taskService;
        this.balanceLogService = balanceLogService;
        this.houseService = houseService;
    }

    @Override
    public User getOrCreateUser(String dtprf) {
        return userRepo.findUserByDtprf(dtprf)
                .orElse(userRepo.save(new User(dtprf, "Игрок")));
    }

    @Override
    public User getOrCreateUser(String dtprf, Group group, LevelSA level) {
        return userRepo.findUserByDtprf(dtprf)
                .orElse(userRepo.save(new User(dtprf, "Игрок", group, level)));
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
        List<String> wasModified = new ArrayList<>();
        for (ImportDto line : importLines) {
            //Получить пользователя для которого происходит импорт
            User user = userRepo
                    .findUserByDtprf(line.getDtprf())
                    .orElse(null);
            if (user == null) continue;

            //Импорт
            if(line instanceof CoinImportDto dto ) {
                //Начисление монет.
                //Обновление
                if(user.getUserGroup() == null || user.getUserLevel() == null)
                    continue;
                updateUserGroupLevel(user, user.getUserGroup(), user.getUserLevel());
                //Получить статус домов
                UserProgress userProgress = UserProgress.fromString(user.getUserProgressJson());
                HouseProgress houseProgress = userProgress.getHouseProgressByHouseMapId(dto.getHouseMapId());
                //Дом не найден
                if(houseProgress == null) {
                    log.error("House to update is not found for user.");
                    continue;
                }
                //Обнулить баланс перед импортом
                if(!wasModified.contains(user.getDtprf())){
                    user.setCoins(0);
                    user.setMaxCoins(0);
                }
                //Внести изменения
                houseProgress.setCurrentCoins(dto.getNewValue());
                houseProgress.setMaxCoins(dto.getMaxValue());
                //Внести изменения в пользователя для общих чисел
                user.setCoins(user.getCoins() + dto.getNewValue());
                user.setMaxCoins(user.getMaxCoins() + dto.getMaxValue());
                //Сохранить статус домов
                user.setUserProgressJson(userProgress.toString());
                //Логирование
                user.getBalanceLog().add(balanceLogService.saveLog(user, Activity.IMPORT, user.getCoins()));
                //Сохранить пользователя
                userRepo.save(user);
                wasModified.add(user.getDtprf());
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
        log.info("{} lines processed, {} lines updated for {} user(s)", importLines.size(), count, wasModified.size());
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

    @Override
    public User saveUserChanges(User user) {
        return userRepo.save(user);
    }

    @Override
    public User updateUserGroupLevel(User user, Group group, LevelSA level) {
        boolean updateRequired = false;
        if(group != null) {
            Group current = user.getUserGroup();
            if(current != group) {
                user.setUserGroup(group);
                updateRequired = true;
            }
        }
        if(level != null) {
            LevelSA current = user.getUserLevel();
            if(current != level) {
                user.setUserLevel(level);
                updateRequired = true;
            }
        }
        UserProgress progress = UserProgress.fromString(user.getUserProgressJson());
        if(updateRequired || progress.getProgressPerHouseList().isEmpty()) {
            List<House> newListOfHouses = houseService.getHousesForGroupAndLevel(group, level);
            progress.addNewHouses(newListOfHouses);
            user.setUserProgressJson(progress.toString());
        }
        return userRepo.save(user);
    }
}
