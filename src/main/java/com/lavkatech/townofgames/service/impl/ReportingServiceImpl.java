package com.lavkatech.townofgames.service.impl;

import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import com.lavkatech.townofgames.entity.report.BalanceLogExportDto;
import com.lavkatech.townofgames.entity.report.CoinImportDto;
import com.lavkatech.townofgames.entity.report.LevelGroupImportDto;
import com.lavkatech.townofgames.entity.report.TasksImportDto;
import com.lavkatech.townofgames.service.BalanceLogService;
import com.lavkatech.townofgames.service.ReportingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ReportingServiceImpl implements ReportingService {

    private static final Logger log = LogManager.getLogger();
    private final static String tempPath = System.getProperty("java.io.tmpdir") + File.separator;
    private final BalanceLogService balanceLogService;

    public ReportingServiceImpl(BalanceLogService balanceLogService) {
        this.balanceLogService = balanceLogService;
    }

    @Override
    public List<CoinImportDto> importCoins(File file) throws IOException {
        List<CoinImportDto> coinData = new ArrayList<>();
        int count = 0;
        try(FileInputStream fis = new FileInputStream(file)) {
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue;

                //0 -> dtprf, 1 -> названия дома, 2 -> максимальное значение, 3 -> новое значение
                String dtprf = "";
                Integer houseMapId = null;
                Integer maxValue = null;
                Integer newValue = null;
                for (Cell cell : row)
                    switch (cell.getColumnIndex()) {
                        case 0 -> dtprf = tryGetStringCellValue(cell);
                        case 1 -> houseMapId = tryGetNumericCellValue(cell);
                        case 2 -> maxValue = tryGetNumericCellValue(cell);
                        case 3 -> newValue = tryGetNumericCellValue(cell);
                    }

                count++;

                if(dtprf == null || dtprf.equals(""))
                    log.error("Importing row {} was skipped because service failed to parse dtprf (dtrpf={})", row.getRowNum(), dtprf);
                else if(houseMapId == null)
                    log.error("Importing row {} was skipped because service failed to parse house map id (dtrpf={})", row.getRowNum(), dtprf);
                else if(maxValue == null)
                    log.error("Importing row {} was skipped because service failed to parse max value (dtrpf={})", row.getRowNum(), dtprf);
                else if(newValue == null)
                    log.error("Importing row {} was skipped because service failed to parse new value (dtrpf={})", row.getRowNum(), dtprf);
                else
                    coinData.add(new CoinImportDto(dtprf, houseMapId, maxValue, newValue));
            }
            log.info("{} rows processed, {} rows imported", count, coinData.size());
        }
        return coinData;
    }

    @Override
    public List<TasksImportDto> importTasks(File file) throws IOException{
        List<TasksImportDto> tasksData = new ArrayList<>();
        int count = 0;
        try(FileInputStream fis = new FileInputStream(file)) {
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue;

                //0 -> dtprf, 1 -> названия дома, 2 -> код миссии, 3 -> флаг выполнения, 4 -> переменная 1, 5 -> переменная 2, 6 -> переменная 3
                String dtprf = "";
                Integer houseMapId = null;
                Integer taskCode = null;
                Boolean isTaskComplete = null;
                String var1 = "";
                String var2 = "";
                String var3 = "";
                for (Cell cell : row)
                    switch (cell.getColumnIndex()) {
                        case 0 -> dtprf = tryGetStringCellValue(cell);
                        case 1 -> houseMapId = tryGetNumericCellValue(cell);
                        case 2 -> taskCode = tryGetNumericCellValue(cell);
                        case 3 -> isTaskComplete = tryGetNumericCellValue(cell) == null ? null : tryGetNumericCellValue(cell) == 1;
                        case 4 -> var1 = tryGetStringCellValue(cell);
                        case 5 -> var2 = tryGetStringCellValue(cell);
                        case 6 -> var3 = tryGetStringCellValue(cell);
                    }

                count++;

                var1 = var1 == null ? "" : var1;
                var2 = var2 == null ? "" : var2;
                var3 = var3 == null ? "" : var3;

                if(dtprf == null || dtprf.equals(""))
                    log.error("Importing row {} was skipped because service failed to parse dtprf (dtrpf={})", row.getRowNum(), dtprf);
                else if(houseMapId == null)
                    log.error("Importing row {} was skipped because service failed to parse house map id (dtrpf={})", row.getRowNum(), dtprf);
                else if(taskCode == null)
                    log.error("Importing row {} was skipped because service failed to parse task code (dtrpf={})", row.getRowNum(), dtprf);
                else if(isTaskComplete == null)
                    log.error("Importing row {} was skipped because service failed to parse task status (dtrpf={})", row.getRowNum(), dtprf);
                else
                    tasksData.add(new TasksImportDto(dtprf, houseMapId, taskCode, isTaskComplete, var1, var2, var3));
            }
            log.info("{} rows processed, {} rows imported", tasksData.size(), count);
        }
        return tasksData;
    }

    @Override
    public List<LevelGroupImportDto> importLevelGroup(File file) throws IOException{
        List<LevelGroupImportDto> levelGroupData = new ArrayList<>();
        int count = 0;
        try(FileInputStream fis = new FileInputStream(file)) {
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue;

                //0 -> dtprf, 1 -> группа, 2 -> уровень
                String dtprf = null;
                Group group = null;
                LevelSA level = null;
                for (Cell cell : row)
                    switch (cell.getColumnIndex()) {
                        case 0 -> dtprf = tryGetStringCellValue(cell);
                        case 1 -> group = tryGetStringCellValue(cell) != null ? Group.valueOf(tryGetStringCellValue(cell).toUpperCase()) : null;
                        case 2 -> level = tryGetStringCellValue(cell) != null ?  LevelSA.valueOf(tryGetStringCellValue(cell).toUpperCase()) : null;
                    }

                count++;

                if(dtprf == null)
                    log.error("Importing row {} was skipped because service failed to parse dtprf (dtrpf={})", row.getRowNum(), dtprf);
                else if(group == null)
                    log.error("Importing row {} was skipped because service failed to parse level (dtrpf={})", row.getRowNum(), dtprf);
                else if(level == null)
                    log.error("Importing row {} was skipped because service failed to parse group (dtrpf={})", row.getRowNum(), dtprf);
                else
                    levelGroupData.add(new LevelGroupImportDto(dtprf, group, level));
            }
            log.info("{} rows processed, {} rows imported", count, levelGroupData.size());
        }
        return levelGroupData;
    }

    @Override
    public File exportActivity() throws IOException {
        return null;
    }

    @Override
    public File exportBalance() throws IOException {
        try {
            //Create report data
            List<BalanceLogExportDto> report = balanceLogService.createReport();
            //Create report file
            String pathToFile = createReportFile(report);
            //Send created file to user
            return new File(pathToFile);
        } catch (Exception e) {
            log.error("Error occurred while creating report", e);
            return null;
        }
    }

    private String createReportFile(List<BalanceLogExportDto> lines) throws IOException {
        //Имя
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMdd_HHmmss");
        String newFilePath = tempPath + "Report_" + dtf.format(LocalDateTime.now()) + ".xlsx";
        //Создание файла
        try(OutputStream fos = new FileOutputStream(newFilePath)) {
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("Report");

            int n = lines.size();
            //Создание строчек соответственно кол-ву точек
            for (int i = 0; i < n + 1; i++) sheet.createRow(i);

            Iterator<BalanceLogExportDto> it = lines.listIterator();
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    //Создание наименований столбцов
                    createHeader(wb);
                    continue;
                }

                if (!it.hasNext()) break;

                //Запись данных
                BalanceLogExportDto line = it.next();
                row.createCell(0).setCellValue(String.valueOf(line.getId()));
                row.createCell(1).setCellValue(line.getDtprf());
                row.createCell(2).setCellValue(line.getGroup());
                row.createCell(3).setCellValue(line.getActivity());
                row.createCell(4).setCellValue(line.getBalance());
                row.createCell(5).setCellValue(line.getTimestamp());
                row.createCell(6).setCellValue(line.getLevel());
            }

            //Запись в файл
            wb.write(fos);
        }
        return newFilePath;
    }

    private static void createHeader(XSSFWorkbook wb) {
        XSSFSheet sheet = wb.getSheet("Report");
        Row header = sheet.getRow(0);

        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("DTPRF");
        header.createCell(2).setCellValue("Номер карты");
        header.createCell(3).setCellValue("Тип операции");
        header.createCell(4).setCellValue("Фактический баланс монет");
        header.createCell(5).setCellValue("Дата/время изменения баланса");
        header.createCell(6).setCellValue("Уровень SA");
    }

    private Integer tryGetNumericCellValue(Cell cell) {
        if (cell == null) return null;
        Integer val = null;
        try {
            val = (int) cell.getNumericCellValue();
        } catch (IllegalStateException ise) {
            try {
                val = Integer.parseInt(cell.getStringCellValue());
            } catch (NumberFormatException nfe) {
                log.error("Value in cell:{} row:{} is not valid", cell.getAddress().getColumn(), cell.getRow().getRowNum() + 1, nfe);
            }
        }
        return val;
    }

    private String tryGetStringCellValue(Cell cell) {
        if (cell == null) return null;
        String val = null;
        try {
            val = cell.getStringCellValue();
        } catch (IllegalStateException ise) {
            try {
                val = String.valueOf(cell.getNumericCellValue());
            } catch (NumberFormatException nfe) {
                log.error("Value in cell:{} row:{} is not valid", cell.getAddress().getColumn(), cell.getRow().getRowNum() + 1, nfe);
            }
        }
        return val;
    }


}
