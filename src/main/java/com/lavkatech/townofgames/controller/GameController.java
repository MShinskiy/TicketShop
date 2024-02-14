package com.lavkatech.townofgames.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lavkatech.townofgames.entity.House;
import com.lavkatech.townofgames.entity.User;
import com.lavkatech.townofgames.entity.dto.HouseStatusDto;
import com.lavkatech.townofgames.entity.dto.MapDto;
import com.lavkatech.townofgames.entity.dto.UserDto;
import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import com.lavkatech.townofgames.service.HouseService;
import com.lavkatech.townofgames.service.HouseVisitLogService;
import com.lavkatech.townofgames.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.lavkatech.townofgames.misc.Util.decrypt;
import static com.lavkatech.townofgames.misc.Util.encrypt;

@Controller
public class GameController {

    @Value("${cipher.initVector}")
    private String initVector;
    @Value("${cipher.key}")
    private String key;
    @Value("${message.error}")
    private String genericErrorMessage;

    private static final Logger log = LogManager.getLogger();
    private final UserService userService;
    private final HouseService houseService;
    private final HouseVisitLogService houseVisitLogService;

    @Autowired
    public GameController(UserService userService, HouseService houseService, HouseVisitLogService houseVisitLogService) {
        this.userService = userService;
        this.houseService = houseService;
        this.houseVisitLogService = houseVisitLogService;
    }

    @PostMapping("/demo")
    public String getDemoScreen(@RequestParam (name = "query") String query, Model model) {
        //System.out.println(query);
        //Decipher query parameter
        if(query.startsWith("query="))
            query = query.replace("query=", "");
        String res;
        String param = URLDecoder.decode(query.replace("query=", ""), StandardCharsets.UTF_8);
        res = decrypt(param, initVector, key);
        if(res == null) {
            res = decrypt(query, initVector, key);
        }
        if(res == null) {
            log.error("Could not decrypt query {} with vector {} and key {}", param, initVector, key);
            model.addAttribute("errorMsg", genericErrorMessage);
            return "error";
        }
        //Get user params
        try {
            JsonObject o = new Gson().fromJson(res, JsonObject.class);
            JsonObject contactObj = o
                    .get("User").getAsJsonObject()
                    .get("Contact").getAsJsonObject();
            String dtprf = contactObj.get("DTE_Contact_Id__c").getAsString();
            String group = contactObj.get("mapID").getAsString();
            String level = contactObj.get("levelSA").getAsString();

            User user = new User(dtprf, "demo username", Group.valueOf(group.toUpperCase()), LevelSA.valueOf(level.toUpperCase()));

            if(user == null) {
                log.error("User with dtprf {} was not found", dtprf);
                model.addAttribute("errorMsg", String.format("User with dtprf %s was not found", dtprf));
                return "error";
            }

            //Parse group from query
            Group g;
            if(group == null) {
                g = user.getUserGroup();
                if(g == null) {
                    log.error("Group not found for dtprf {}", dtprf);
                    model.addAttribute("errorMsg", String.format("Group not found for dtprf %s", dtprf));
                    return "error";
                }
            } else {
                try {
                    g = Group.valueOf(group.toUpperCase());
                } catch (IllegalArgumentException iae) {
                    log.error("Error while parsing {} to Group for dtprf {}", group, dtprf);
                    model.addAttribute("errorMsg", String.format("Error while parsing %s to Group for dtprf %s", group, dtprf));
                    return "error";
                }
            }

            //Parse level from query
            LevelSA l;
            if(level == null) {
                l = user.getUserLevel();
                if(l == null) {
                    log.error("Level not found for dtprf {}", dtprf);
                    model.addAttribute("errorMsg", String.format("User with dtprf %s was not found", dtprf));
                    return "error";
                }
            } else {
                try {
                    l = LevelSA.valueOf(level.toUpperCase());
                } catch (IllegalArgumentException iae) {
                    log.error("Error while parsing {} to LevelSA", level);
                    model.addAttribute("errorMsg", String.format("User with dtprf %s was not found", dtprf));
                    return "error";
                }
            }

            user = userService.updateUserGroupLevel(user, g, l);
            user = userService.updateUserProgressTasks(user);
            Map<Integer, HouseStatusDto> houses = houseService.getHousesDtosForUserWithGroupAndLevel(user, g, l);
            UserDto userDto = user.toDto();
            MapDto map = new MapDto(userDto, houses);
            model.addAttribute("json", map.toJsonString());
            //Send users map
            return "index";
        } catch (NullPointerException e) {
            log.error("Could not parse json string {} ", query, e);
            model.addAttribute("errorMsg", genericErrorMessage);
            //Send error frame on error
            return "error";
        }
    }

    @PostMapping("/play")
    public String getHomeScreen(@RequestBody String query, Model model) {
        if(query.isEmpty()) {
            log.error("Empty query");
            model.addAttribute("errorMsg", genericErrorMessage);
            return "error";
        }
        //Decipher query parameter
        if(query.startsWith("query="))
            query = query.replace("query=", "");
        String res;
        String param = URLDecoder.decode(query.replace("query=", ""), StandardCharsets.UTF_8);
        res = decrypt(param, initVector, key);
        if(res == null)
            res = decrypt(query, initVector, key);
        if(res == null) {
            log.error("Could not decrypt query {} with vector {} and key {}", param, initVector, key);
            model.addAttribute("errorMsg", genericErrorMessage);
            return "error";
        }
        //Get user params
        try {
            JsonObject o = new Gson().fromJson(res, JsonObject.class);
            JsonObject contactObj = o
                    .get("User").getAsJsonObject()
                    .get("Contact").getAsJsonObject();
            String dtprf = contactObj.get("DTE_Contact_Id__c").getAsString();
            String group = contactObj.get("mapID").getAsString();
            String level = contactObj.get("levelSA").getAsString();


            User user = userService.getOrNull(dtprf);

            if(user == null)
                user = userService.createUser(dtprf);


            //Parse group from query
            Group g;
            if(group == null) {
                g = user.getUserGroup();
                if(g == null) {
                    log.error("Group not found for dtprf {}", dtprf);
                    //return "error";
                }
            } else {
                try {
                    g = Group.valueOf(group.toUpperCase());
                } catch (IllegalArgumentException iae) {
                    log.error("Error while parsing {} to Group for dtprf {}", group, dtprf);
                    return "error";
                }
            }

            //Parse level from query
            LevelSA l;
            if(level == null) {
                l = user.getUserLevel();
                if(l == null) {
                    log.error("Level not found for dtprf {}", dtprf);
                    //return "error";
                }
            } else {
                l = level.equalsIgnoreCase("HIGH") ? LevelSA.HIGH : LevelSA.LOW;
            }

            user = userService.updateUserGroupLevel(user,g , l);
            user = userService.updateUserProgressTasks(user);
            Map<Integer, HouseStatusDto> houses = houseService.getHousesDtosForUserWithGroupAndLevel(user, g, l);
            //List<UUID> userHouses = houseService.getHousesForGroupAndLevel(g, l).stream().map(House::getId).toList();
            //UserDto userDto = user.toDto(userHouses);
            UserDto userDto = user.toDto();
            MapDto map = new MapDto(userDto, houses);
            model.addAttribute("json", map.toJsonString());

            user.setLastLogin(LocalDateTime.now());
            user = userService.saveUserChanges(user);

            houseVisitLogService.saveLog(user, g, l);
            //Send users map
            return "index";
        } catch (NullPointerException e) {
            log.error("Could not parse json string:\n {} \nError:\n", query, e);
            //Send error frame on error
            return "error";
        }
    }

    @GetMapping("/user/request")
    public @ResponseBody String getUsersView(@RequestParam String dtprf, @RequestParam String group, @RequestParam String level) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        /*User user = userService.getOrNull(dtprf);
        if(user == null) return String.format("""
                {
                  "Timestamp": "2024-02-01 09:02:51",
                  "User": {
                    "Contact": {
                      "DTE_Contact_Id__c": "%s",
                      "mapID": "null",
                      "levelSA": "null"
                    }
                  }
                }
                """, dtprf.toUpperCase());*/

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
                """, dtprf.toUpperCase(), group.toUpperCase(), level.toUpperCase());

        return encrypt(req, key, initVector);
    }
}
