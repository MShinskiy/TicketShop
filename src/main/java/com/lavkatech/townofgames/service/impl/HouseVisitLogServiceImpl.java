package com.lavkatech.townofgames.service.impl;

import com.lavkatech.townofgames.entity.HouseVisitLog;
import com.lavkatech.townofgames.entity.User;
import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import com.lavkatech.townofgames.entity.report.HouseVisitLogExportDto;
import com.lavkatech.townofgames.repository.HouseVisitLogRepository;
import com.lavkatech.townofgames.service.HouseVisitLogService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HouseVisitLogServiceImpl implements HouseVisitLogService {

    @Value("${time.format}")
    private String dateTimeFormat;
    private final HouseVisitLogRepository houseVisitLogRepo;

    public HouseVisitLogServiceImpl(HouseVisitLogRepository houseVisitLogRepo) {
        this.houseVisitLogRepo = houseVisitLogRepo;
    }

    @Override
    public void saveLog(User user, Group group, LevelSA level) {
        houseVisitLogRepo.save(createLog(user, group, level));
    }

    @Override
    public HouseVisitLog createLog(User user, Group loginGroup, LevelSA loginLevel) {
        /*
        * TODO Получить лог по каждому дому
        * */
        return houseVisitLogRepo.save(HouseVisitLog.builder()
                        .loginGroup(loginGroup)
                        .loginLevel(loginLevel)
                        .loginTimestamp(user.getLastLogin())
                .house1(null)
                .house2(null)
                .house3(null)
                .house4(null)
                .house5(null)
                .house6(null)
                .house7(null)
                .house8(null)
                .house9(null)
                .user(user)
                .build());
    }

    @Override
    public List<HouseVisitLogExportDto> createReport() {
        return houseVisitLogRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public HouseVisitLogExportDto toDto(HouseVisitLog log) {
        return new HouseVisitLogExportDto(
                log.getUser().getDtprf(),
                log.getLoginGroup().name(),
                log.getLoginLevel().name(),
                log.getLoginTimestamp().format(DateTimeFormatter.ofPattern(dateTimeFormat)),
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
/*                log.getHouse1().format(DateTimeFormatter.ofPattern(dateTimeFormat)),
                log.getHouse2().format(DateTimeFormatter.ofPattern(dateTimeFormat)),
                log.getHouse3().format(DateTimeFormatter.ofPattern(dateTimeFormat)),
                log.getHouse4().format(DateTimeFormatter.ofPattern(dateTimeFormat)),
                log.getHouse5().format(DateTimeFormatter.ofPattern(dateTimeFormat)),
                log.getHouse6().format(DateTimeFormatter.ofPattern(dateTimeFormat)),
                log.getHouse7().format(DateTimeFormatter.ofPattern(dateTimeFormat)),
                log.getHouse8().format(DateTimeFormatter.ofPattern(dateTimeFormat)),
                log.getHouse9().format(DateTimeFormatter.ofPattern(dateTimeFormat))*/
                );
    }
}
