package com.lavkatech.townofgames.service.impl;

import com.lavkatech.townofgames.entity.BalanceLog;
import com.lavkatech.townofgames.entity.User;
import com.lavkatech.townofgames.entity.enums.Activity;
import com.lavkatech.townofgames.entity.report.dto.BalanceLogExportDto;
import com.lavkatech.townofgames.repository.BalanceLogRepository;
import com.lavkatech.townofgames.service.BalanceLogService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BalanceLogServiceImpl implements BalanceLogService {

    private final BalanceLogRepository balanceLogRepo;
    @Value("${time.format}")
    private String dateTimeFormat;

    public BalanceLogServiceImpl(BalanceLogRepository balanceLogRepo) {
        this.balanceLogRepo = balanceLogRepo;
    }

    @Override
    public BalanceLog saveLog(BalanceLog log) {
        return balanceLogRepo.save(log);
    }

    @Override
    public BalanceLog saveLog(User user, Activity activity, long total) {
        return balanceLogRepo.save(createLog(user, activity, total));
    }

    @Override
    public BalanceLog createLog(User user, Activity activity, long total) {
        return BalanceLog.builder()
                .activity(activity)
                .totalBalance(total)
                .mapGroup(user.getUserGroup())
                .mapLevel(user.getUserLevel())
                .timestamp(LocalDateTime.now())
                .user(user)
                .build();
    }

    @Override
    public List<BalanceLogExportDto> createReport() {
        return balanceLogRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    private BalanceLogExportDto toDto(BalanceLog log) {
        return new BalanceLogExportDto(
                log.getUser().getDtprf(),
                log.getId(),
                log.getMapGroup().name(),
                log.getMapLevel().name(),
                log.getActivity().name(),
                (int) log.getTotalBalance(),
                log.getTimestamp().format(DateTimeFormatter.ofPattern(dateTimeFormat))
        );
    }

}
