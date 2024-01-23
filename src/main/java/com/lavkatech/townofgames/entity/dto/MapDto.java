package com.lavkatech.townofgames.entity.dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public record MapDto(UserDto user, List<HouseStatusDto> map) {

    public String toJsonString(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
