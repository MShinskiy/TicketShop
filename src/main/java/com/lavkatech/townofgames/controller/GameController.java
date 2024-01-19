package com.lavkatech.townofgames.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lavkatech.townofgames.entity.User;
import com.lavkatech.townofgames.entity.dto.HouseStatusDto;
import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.service.HouseService;
import com.lavkatech.townofgames.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

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


    @GetMapping("/play")
    public String getHomeScreen(@RequestBody String query, Model model) {
        //Decipher query parameter
        String param = URLDecoder.decode(query.replaceAll("query=", ""), StandardCharsets.UTF_8);
        query = decrypt(param, initVector, key);
        //Get user params
        try {
            JsonObject o = new Gson().fromJson(query, JsonObject.class);
            String dtprf = o
                    .get("User").getAsJsonObject()
                    .get("Contact").getAsJsonObject()
                    .get("DTE_Contact_Id__c").getAsString();

            User user = userService.getUser(dtprf);
            if(user == null) {
                /* TODO handle user doesn't exist
                *   create or don't allow entry? */
                return "error";
            }

            Group userGroup = user.getUserGroup();
            if(userGroup == null) {
                /* TODO handle client not
                    providing user group */
                return "error";
            }

            List<HouseStatusDto> dtosToFront = houseService.getHousesDtosForUser(user);
            Gson gson = new Gson();
            model.addAttribute("HouseStateJSON", gson.toJson(dtosToFront));
            model.addAttribute("UserStateJSON", gson.toJson(user.toDto()));
            //Send current set broadcast to a newly connected user
            return "index";
        } catch (NullPointerException e) {
            log.error("Could not parse json string {} ", query, e);
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
