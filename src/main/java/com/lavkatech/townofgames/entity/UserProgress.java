package com.lavkatech.townofgames.entity;

import com.google.gson.Gson;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class UserProgress {

    private final Map<UUID, TasksProgress> progressPerHouseMap;

    public UserProgress() {
        this.progressPerHouseMap = new HashMap<>();
    }

    public static UserProgress fromString(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, UserProgress.class);
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this.progressPerHouseMap);
    }
}


