package com.lavkatech.townofgames.controller;

import com.lavkatech.townofgames.entity.House;
import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.enums.HouseName;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import com.lavkatech.townofgames.entity.report.*;
import com.lavkatech.townofgames.service.HouseService;
import com.lavkatech.townofgames.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Controller
public class AdminController {

    private final HouseService houseService;
    private final UserService userService;
    private static final Logger log = LogManager.getLogger();
    private final static String tempPath = System.getProperty("java.io.tmpdir") + File.separator;

    public AdminController(HouseService houseService, UserService userService) {
        this.houseService = houseService;
        this.userService = userService;
    }

    @GetMapping("/administration/edit")
    public String getEditingPanel(Model model) {
        List<House> houses = houseService.getAllHouses();
        model.addAttribute("houses", houses);
        return "edit-panel";
    }

    @PostMapping("/administration/upload")
    public String fileImport(Model model, @RequestParam("file") MultipartFile file,  @RequestParam("import-type") String importType) {
        if(file.isEmpty()) {
            model.addAttribute("msgUld", "Файл пустой или отсутствует");
            log.debug("Importing file is empty");
            return "admin-panel";
        }
        String filename = file.getOriginalFilename();
        String newFilename = generateRandomFilename(32) + ".xlsx";
        File tempFile = new File(tempPath + File.separator + newFilename);

        try {
            Files.copy(
                    file.getInputStream(),
                    tempFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );
            log.info("Import file downloaded with name {}", tempFile.getName());
        } catch (IOException e) {
            log.error("Import file with name {} could not be downloaded", tempFile.getName(), e);
        }

        //Update users from file
        try {
            switch (ImportType.valueOf(importType)) {
                case COINS -> {importCoins(tempFile);}
                case TASKS -> {importTasks(tempFile);}
                case LEVEL_GROUP -> {importLevelGroup(tempFile);}
            }
            List<ImportDto> parsedMap = parseFile(tempFile);
            userService.updateUsers(parsedMap);
            /*for (String dtprf : parsedMap.keySet())
                userService.updateUserMoves(dtprf, parsedMap.get(dtprf));*/

            //Log and notify admin
            model.addAttribute("msgUld", String.format("Файл %s загружен и импортирован.", filename));
            return "admin-panel";
        } catch (Exception e) {
            log.error("Error occurred while importing data from file with name {}", tempFile.getName(), e);
            model.addAttribute("msgUld", String.format("Во время импортирования файла %s произошла ошибка", filename));
            return "admin-panel";
        } finally {
            boolean isDeleted = tempFile.delete();
            log.debug("Temp file with name {} has been deleted: {}", tempFile.getName(), isDeleted);
        }
    }

    private List<ImportDto> parseFile(File xlsx) throws IOException{
        List<ImportDto> userData = new ArrayList<>();
        int count = 0;
        try(FileInputStream fis = new FileInputStream(xlsx)) {
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0)
                    continue;

                //0 -> dtprf, 1 -> монеты, 2 -> баллы, 3 -> задания, 4 -> карта
                String dtprf = "";
                Integer nCoinsToAdd = null;
                Integer nPointsToAdd = null;
                Integer nTasksToAdd = null;
                Group group = null;
                for (Cell cell : row)
                    switch (cell.getColumnIndex()) {
                        case 0 -> dtprf = cell.getStringCellValue();
                        case 1 -> nCoinsToAdd = tryGetNumericCellValue(cell);
                        case 2 -> nPointsToAdd = tryGetNumericCellValue(cell);
                        case 3 -> nTasksToAdd = tryGetNumericCellValue(cell);
                        case 4 -> group = Group.groupOf(tryGetNumericCellValue(cell));
                    }

                count++;

                if(group == null)
                    log.error("Importing row {} was skipped because service failed to parse group (dtrpf={})", row.getRowNum(), dtprf);
                else if( dtprf == null || dtprf.equals(""))
                    log.error("Importing row {} was skipped because service failed to parse dtprf (dtrpf={})", row.getRowNum(), dtprf);
                else if( nCoinsToAdd == null)
                    log.error("Importing row {} was skipped because service failed to parse coins (dtrpf={})", row.getRowNum(), dtprf);
                else if( nPointsToAdd == null)
                    log.error("Importing row {} was skipped because service failed to parse points (dtrpf={})", row.getRowNum(), dtprf);
                else if( nTasksToAdd == null)
                    log.error("Importing row {} was skipped because service failed to parse tasks (dtrpf={})", row.getRowNum(), dtprf);
                else
                    userData.add(new ImportDto(dtprf, nCoinsToAdd, nPointsToAdd, nTasksToAdd, group));
            }
            log.info("{} rows processed, {} rows imported", count, userData.size());
        }
        return userData;
    }

    private List<CoinImportDto> importCoins(File file) throws IOException{
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
                HouseName houseName = null;
                Integer maxValue = null;
                Integer newValue = null;
                for (Cell cell : row)
                    switch (cell.getColumnIndex()) {
                        case 0 -> dtprf = cell.getStringCellValue();
                        case 1 -> houseName = HouseName.valueOf(cell.getStringCellValue());
                        case 2 -> maxValue = tryGetNumericCellValue(cell);
                        case 3 -> newValue = tryGetNumericCellValue(cell);
                    }

                count++;

                if(dtprf == null || dtprf.equals(""))
                    log.error("Importing row {} was skipped because service failed to parse dtprf (dtrpf={})", row.getRowNum(), dtprf);
                else if(houseName == null)
                    log.error("Importing row {} was skipped because service failed to parse house name (dtrpf={})", row.getRowNum(), dtprf);
                else if(maxValue == null)
                    log.error("Importing row {} was skipped because service failed to parse max value (dtrpf={})", row.getRowNum(), dtprf);
                else if(newValue == null)
                    log.error("Importing row {} was skipped because service failed to parse new value (dtrpf={})", row.getRowNum(), dtprf);
                else
                    coinData.add(new CoinImportDto(dtprf, houseName, maxValue, newValue));
            }
            log.info("{} rows processed, {} rows imported", count, coinData.size());
        }
        return coinData;
    }

    private List<TasksImportDto> importTasks(File file) throws IOException{
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
                HouseName houseName = null;
                Integer taskCode = null;
                Boolean isTaskComplete = null;
                String var1 = "";
                String var2 = "";
                String var3 = "";
                for (Cell cell : row)
                    switch (cell.getColumnIndex()) {
                        case 0 -> dtprf = cell.getStringCellValue();
                        case 1 -> houseName = HouseName.valueOf(cell.getStringCellValue());
                        case 2 -> taskCode = tryGetNumericCellValue(cell);
                        case 3 -> isTaskComplete = tryGetNumericCellValue(cell) == null ? null : tryGetNumericCellValue(cell) == 1;
                        case 4 -> var1 = cell.getStringCellValue();
                        case 5 -> var2 = cell.getStringCellValue();
                        case 6 -> var3 = cell.getStringCellValue();
                    }

                count++;

                if(dtprf == null || dtprf.equals(""))
                    log.error("Importing row {} was skipped because service failed to parse dtprf (dtrpf={})", row.getRowNum(), dtprf);
                else if(houseName == null)
                    log.error("Importing row {} was skipped because service failed to parse house name (dtrpf={})", row.getRowNum(), dtprf);
                else if(taskCode == null)
                    log.error("Importing row {} was skipped because service failed to parse task code (dtrpf={})", row.getRowNum(), dtprf);
                else if(isTaskComplete == null)
                    log.error("Importing row {} was skipped because service failed to parse task status (dtrpf={})", row.getRowNum(), dtprf);
                else if(var1 == null)
                    log.error("Importing row {} was skipped because service failed to parse var1 (dtrpf={})", row.getRowNum(), dtprf);
                else if(var2 == null)
                    log.error("Importing row {} was skipped because service failed to parse var2 (dtrpf={})", row.getRowNum(), dtprf);
                else if(var3 == null)
                    log.error("Importing row {} was skipped because service failed to parse var3 (dtrpf={})", row.getRowNum(), dtprf);
                else
                    tasksData.add(new TasksImportDto(dtprf, houseName, taskCode, isTaskComplete, var1, var2, var3));
            }
            log.info("{} rows processed, {} rows imported", count, tasksData.size());
        }
        return tasksData;
    }

private List<LevelGroupImportDto> importLevelGroup(File file) throws IOException{
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
                        case 0 -> dtprf = cell.getStringCellValue();
                        case 1 -> group = Group.groupOf(tryGetNumericCellValue(cell));
                        case 2 -> level = LevelSA.levelOf(cell.getStringCellValue());
                    }

                count++;

                if(dtprf == null)
                    log.error("Importing row {} was skipped because service failed to parse dtprf (dtrpf={})", row.getRowNum(), dtprf);
                else if(group == null)
                    log.error("Importing row {} was skipped because service failed to parse house name (dtrpf={})", row.getRowNum(), dtprf);
                else if(level == null)
                    log.error("Importing row {} was skipped because service failed to parse task code (dtrpf={})", row.getRowNum(), dtprf);
                else
                    levelGroupData.add(new LevelGroupImportDto(dtprf, group, level));
            }
            log.info("{} rows processed, {} rows imported", count, levelGroupData.size());
        }
        return levelGroupData;
    }


    private Integer tryGetNumericCellValue(Cell cell) {
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

    private String generateRandomFilename(int len) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(len)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
