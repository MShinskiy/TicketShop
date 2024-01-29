package com.lavkatech.townofgames.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lavkatech.townofgames.entity.User;
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

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Controller
public class GameController {

    @Value("${cipher.initVector}")
    private String initVector;
    @Value("${cipher.key}")
    private String key;
    private static final Logger log = LogManager.getLogger();
    private final UserService userService;
    private final HouseService houseService;

    @Autowired
    public GameController(UserService userService, HouseService houseService) {
        this.userService = userService;
        this.houseService = houseService;
    }


    @PostMapping("/play")
    public String getHomeScreen(@RequestBody String query, Model model) {
        //Decipher query parameter
        String param = URLDecoder.decode(query.replaceAll("query=", ""), StandardCharsets.UTF_8);
        query = decrypt(param, initVector, key);
        if(query == null) {
            log.error("Could not decrypt query {} with vector {} and key {}", param, initVector, key);
            model.addAttribute("errorMsg", String.format("Could not decrypt query %s with vector %s and key %s", param, initVector, key));
            return "error";
        }
        //Get user params
        try {
            JsonObject o = new Gson().fromJson(query, JsonObject.class);
            JsonObject contactObj = o
                    .get("User").getAsJsonObject()
                    .get("Contact").getAsJsonObject();
            String dtprf = contactObj.get("DTE_Contact_Id__c").getAsString();
            String group = contactObj.get("mapID").getAsString();
            String level = contactObj.get("levelSA").getAsString();

           /* User user = dtprf.equals("DEMO") ?
                    User.builder()
                            .dtprf(dtprf)
                            .username("demo username")
                            .userGroup(Group.PARTNER)
                            .userLevel(LevelSA.HIGH)
                            .coins(2000)
                            .points(5000)
                            .build() :
                    userService.getUserOrNull(dtprf);
            if(user == null) {
                *//* TODO handle user doesn't exist
                *   create or don't allow entry? *//*
                model.addAttribute("errorMsg", String.format("No user was found for dtprf %s", dtprf));
                return "error";
            }

            //Parse group from query
            Group g;
            if(group == null) {
                g = user.getUserGroup();
                if(g == null) {
                    model.addAttribute("errorMsg", String.format("No group was found for user with dtprf %s", dtprf));
                    return "error";
                }
            } else {
                try {
                    g = Group.valueOf(group.toUpperCase());
                } catch (IllegalArgumentException iae) {
                    log.error("Error while parsing {} to Group", group);
                    model.addAttribute("errorMsg", String.format("Error while parsing %s to Group", group));
                    return "error";
                }
            }

            //Parse level from query
            LevelSA l;
            if(level == null) {
                l = user.getUserLevel();
                if(l == null) {
                    model.addAttribute("errorMsg", String.format("No level was found for user with dtprf %s", dtprf));
                    return "error";
                }
            } else {
                try {
                    l = LevelSA.valueOf(level.toUpperCase());
                } catch (IllegalArgumentException iae) {
                    log.error("Error while parsing {} to LevelSA", level);
                    model.addAttribute("errorMsg", String.format("Error while parsing %s to LevelSA", level));
                    return "error";
                }
            }*/

            /*

            Group userGroup = user.getUserGroup();
            if(userGroup == null) {
                *//* TODO handle client not
                    providing user group *//*
                return "error";
            }

            List<HouseStatusDto> houses = houseService.getHousesDtosForUser(user);
            UserDto userDto = user.toDto();
            MapDto map = new MapDto(userDto, houses);
            model.addAttribute("map", map.toJsonString());*/
            //Send current set broadcast to a newly connected user
            return "index";
        } catch (NullPointerException e) {
            log.error("Could not parse json string {} ", query, e);
            model.addAttribute("errorMsg", String.format("Could not parse json string %s ", query));
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
