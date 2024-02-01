package com.lavkatech.townofgames.service;

import com.lavkatech.townofgames.entity.HouseVisitLog;
import com.lavkatech.townofgames.entity.User;
import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import com.lavkatech.townofgames.entity.report.HouseVisitLogExportDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HouseVisitLogService {
    void saveLog(User user, Group group, LevelSA level);
    HouseVisitLog createLog(User user, Group loginGroup, LevelSA loginLevel);
    List<HouseVisitLogExportDto> createReport();
}
