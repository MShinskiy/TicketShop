package com.lavkatech.townofgames.controller;

import com.lavkatech.townofgames.entity.House;
import com.lavkatech.townofgames.service.HouseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminController {

    private final HouseService houseService;

    public AdminController(HouseService houseService) {
        this.houseService = houseService;
    }

    @GetMapping("/administration/edit")
    public String getEditingPanel(Model model) {
        List<House> houses = houseService.getAllHouses();
        model.addAttribute("houses", houses);
        return "edit-panel";
    }
}
