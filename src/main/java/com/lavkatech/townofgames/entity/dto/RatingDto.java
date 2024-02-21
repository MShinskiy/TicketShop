package com.lavkatech.townofgames.entity.dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

public record RatingDto(int userPosition, String region, String validDate, List<RatingLineDto> records) {

    public String toJsonString(){
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}
