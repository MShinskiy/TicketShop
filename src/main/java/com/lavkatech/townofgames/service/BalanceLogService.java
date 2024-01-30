package com.lavkatech.townofgames.service;

import com.lavkatech.townofgames.entity.BalanceLog;
import com.lavkatech.townofgames.entity.User;
import com.lavkatech.townofgames.entity.enums.Activity;
import com.lavkatech.townofgames.entity.report.BalanceLogExportDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BalanceLogService {
    BalanceLog saveLog(BalanceLog log);
    BalanceLog saveLog(User user, Activity activity, long gain);
    BalanceLog createLog(User user, Activity activity, long gain);
    List<BalanceLogExportDto> createReport();
}
