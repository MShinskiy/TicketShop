package com.lavkatech.townofgames.controller;

import com.lavkatech.townofgames.entity.User;
import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.enums.HouseName;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import com.lavkatech.townofgames.service.HouseService;
import com.lavkatech.townofgames.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static com.lavkatech.townofgames.misc.Util.encrypt;

@RestController
public class DataController {
    @Value("${cipher.initVector}")
    private String initVector;
    @Value("${cipher.key}")
    private String key;
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
        /*userService.createUser(
                dtprf,
                username,
                Group.valueOf(group.toUpperCase()),
                LevelSA.valueOf(level.toUpperCase()));*/
    }

    @GetMapping("/data/get/user/post-request")
    public String getUsersView(@RequestParam String dtprf) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        User user = userService.getOrNull(dtprf);
        if(user== null) return "";

        String req = String.format("""
                {
                  "Timestamp": "2024-02-01 09:02:51",
                  "User": {
                    "Contact": {
                      "DTE_Contact_Id__c": "%s",
                      "mapID": "%s",
                      "levelSA": "%s"
                    }
                  }
                }
                """, dtprf.toUpperCase(), user.getUserGroup().name(), user.getUserLevel().name());

        return encrypt(req, key, initVector);
    }
}
