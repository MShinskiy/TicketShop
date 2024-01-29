package com.lavkatech.townofgames.controller;

import com.google.gson.Gson;
import com.lavkatech.townofgames.entity.House;
import com.lavkatech.townofgames.entity.dto.HouseChangesDto;
import com.lavkatech.townofgames.entity.report.*;
import com.lavkatech.townofgames.service.HouseService;
import com.lavkatech.townofgames.service.ReportingService;
import com.lavkatech.townofgames.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Controller
public class AdminController {

    private final ReportingService reportingService;
    private final HouseService houseService;
    private final UserService userService;
    private static final Logger log = LogManager.getLogger();
    private final static String tempPath = System.getProperty("java.io.tmpdir") + File.separator;

    public AdminController(ReportingService reportingService, HouseService houseService, UserService userService) {
        this.reportingService = reportingService;
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
            log.error("Importing file is empty");
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
            List<? extends ImportDto> lines;
            switch (ImportType.valueOf(importType)) {
                case COINS ->       lines = reportingService.importCoins(tempFile);
                case TASKS ->       lines = reportingService.importTasks(tempFile);
                case LEVEL_GROUP -> lines = reportingService.importLevelGroup(tempFile);
                default -> {
                    log.error("Unknown import type for {}", importType);
                    model.addAttribute("msgUld", "Произошла непредвиденная ошибка.");
                    return "admin-panel";
                }
            }
            //Do update
            userService.updateUsers(lines);

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

    @PostMapping("/administration/edit")
    public String updateHouseData(Model model) {
        model.addAttribute("");
        return "edit-panel";
    }

    @PostMapping("/administration/demo")
    public String viewChangesDemo(HouseChangesDto dto, Model model) {
        Gson gson = new Gson();
        String json = gson.toJson(dto.toMapDto());
        model.addAttribute("json", json);
        return "index";
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
