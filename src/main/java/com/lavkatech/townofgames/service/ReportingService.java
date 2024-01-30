package com.lavkatech.townofgames.service;

import com.lavkatech.townofgames.entity.report.CoinImportDto;
import com.lavkatech.townofgames.entity.report.LevelGroupImportDto;
import com.lavkatech.townofgames.entity.report.TasksImportDto;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public interface ReportingService {

    List<CoinImportDto> importCoins(File xlsx) throws IOException;
    List<TasksImportDto> importTasks(File xlsx) throws IOException;
    List<LevelGroupImportDto> importLevelGroup(File xlsx) throws IOException;
    File exportActivity() throws IOException;
    File exportBalance() throws IOException;

}
