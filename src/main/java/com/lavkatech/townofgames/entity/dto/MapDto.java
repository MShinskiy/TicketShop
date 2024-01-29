package com.lavkatech.townofgames.entity.dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

public record MapDto(UserDto user, Map<Integer, HouseStatusDto> map) {

    public String toJsonString(){
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
