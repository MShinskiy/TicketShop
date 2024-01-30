package com.lavkatech.townofgames.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lavkatech.townofgames.entity.User;
import com.lavkatech.townofgames.entity.dto.HouseStatusDto;
import com.lavkatech.townofgames.entity.dto.MapDto;
import com.lavkatech.townofgames.entity.dto.UserDto;
import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import com.lavkatech.townofgames.service.HouseService;
import com.lavkatech.townofgames.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

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

    @Autowired
    public GameController(UserService userService, HouseService houseService) {
        this.userService = userService;
        this.houseService = houseService;
    }

    @PostMapping("/demo")
    public String getDemoScreen(@RequestParam (name = "query") String query, Model model) {
        //System.out.println(query);
        //Decipher query parameter
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
        //Decipher query parameter
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


            User user = dtprf.equals("DEMO") ?
                    new User("DEMO", "demo username", Group.PARTNER, LevelSA.HIGH) :
                    userService.getOrCreateUser(dtprf);

            //Parse group from query
            Group g;
            if(group == null) {
                g = user.getUserGroup();
                if(g == null) {
                    log.error("Group not found for dtprf {}", dtprf);
                    //model.addAttribute("errorMsg", genericErrorMessage);
                    return "error";
                }
            } else {
                try {
                    g = Group.valueOf(group.toUpperCase());
                } catch (IllegalArgumentException iae) {
                    log.error("Error while parsing {} to Group for dtprf {}", group, dtprf);
                    //model.addAttribute("errorMsg", genericErrorMessage);
                    return "error";
                }
            }

            //Parse level from query
            LevelSA l;
            if(level == null) {
                l = user.getUserLevel();
                if(l == null) {
                    log.error("Level not found for dtprf {}", dtprf);
                    //model.addAttribute("errorMsg", genericErrorMessage);
                    return "error";
                }
            } else {
                try {
                    l = LevelSA.valueOf(level.toUpperCase());
                } catch (IllegalArgumentException iae) {
                    log.error("Error while parsing {} to LevelSA", level);
                    //model.addAttribute("errorMsg", genericErrorMessage);
                    return "error";
                }
            }

            user = userService.updateUserGroupLevel(user, g, l);
            Map<Integer, HouseStatusDto> houses = houseService.getHousesDtosForUserWithGroupAndLevel(user, g, l);
            UserDto userDto = user.toDto();
            MapDto map = new MapDto(userDto, houses);
            model.addAttribute("json", map.toJsonString());
            //Send users map
            return "index";
        } catch (NullPointerException e) {
            log.error("Could not parse json string:\n {} \nError:\n", query, e);
            //model.addAttribute("errorMsg", genericErrorMessage);
            //Send error frame on error
            return "error";
        }
    }

    //AES256CBC Message decryption
    private static String decrypt(String encrypted, String initVector, String key) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec sKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, iv);
            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception ex) {
            log.error(ex);
        }
        //Null on error
        return null;
    }
}
