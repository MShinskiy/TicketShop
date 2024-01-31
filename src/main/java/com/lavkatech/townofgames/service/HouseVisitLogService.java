package com.lavkatech.townofgames.service;

import com.lavkatech.townofgames.entity.HouseVisitLog;
import com.lavkatech.townofgames.entity.User;
import com.lavkatech.townofgames.entity.report.HouseVisitLogExportDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HouseVisitLogService {

    void saveLog(User user);
    HouseVisitLog createLog(User user);

    List<HouseVisitLogExportDto> createReport();
}
