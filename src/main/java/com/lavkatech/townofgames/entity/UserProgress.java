package com.lavkatech.townofgames.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lavkatech.townofgames.misc.LocaleDateTimeAdapter;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class UserProgress {

    private final Map<UUID, TasksProgress> progressPerHouseMap;

    public UserProgress() {
        this.progressPerHouseMap = new HashMap<>();
    }

    public static String initString() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocaleDateTimeAdapter().nullSafe())
                .create();
        return gson.toJson(new UserProgress());
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


