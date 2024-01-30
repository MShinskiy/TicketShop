package com.lavkatech.townofgames.controller;

import com.lavkatech.townofgames.entity.House;
import com.lavkatech.townofgames.entity.User;
import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.enums.HouseName;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import com.lavkatech.townofgames.service.HouseService;
import com.lavkatech.townofgames.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {

    private final UserService userService;
    private final HouseService houseService;

    public DataController(UserService userService, HouseService houseService) {
        this.userService = userService;
        this.houseService = houseService;
    }

    @GetMapping("/data/insert/houses")
    public void insertHouses() {
           for(HouseName e : HouseName.values()) {
               for(Group group : Group.values()) {
                   for(LevelSA level : LevelSA.values()) {
                       if(group == Group.OTHER && (e.id() == 1 || e.id() == 8 || e.id() == 7))
                           continue;
                       houseService.createHouse(e.id(), e.canonicalName(), group, level);
                   }
               }
           }
    }

    @GetMapping("/data/insert/users")
    public void insertUsers(@RequestParam String dtprf,
                            @RequestParam String username,
                            @RequestParam String group,
                            @RequestParam String level) {
        userService.createUser(
                dtprf,
                username,
                Group.valueOf(group.toUpperCase()),
                LevelSA.valueOf(level.toUpperCase()));
    }
}
