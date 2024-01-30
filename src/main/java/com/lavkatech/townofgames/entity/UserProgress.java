package com.lavkatech.townofgames.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lavkatech.townofgames.misc.LocalDateTimeAdapter;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
public class UserProgress {
    //House id - task progress
    private final Map<UUID, HouseProgress> progressPerHouseMap;

    public UserProgress() {
        this.progressPerHouseMap = new HashMap<>();
    }


    public static String initString() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter().nullSafe())
                .create();
        return gson.toJson(new UserProgress());
    }

    public static String initString(List<House> houses) {
        UserProgress userProgress = new UserProgress();
        for(House house : houses)
            userProgress.getProgressPerHouseMap()
                    .put(house.getId(), new HouseProgress(house.getMapId()));


        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter().nullSafe())
                .create();
        return gson.toJson(userProgress);
    }

    public void addNewHouses(List<House> houses) {
        for(House house : houses) {
            if(!progressPerHouseMap.containsKey(house.getId()))
                progressPerHouseMap.put(house.getId(), new HouseProgress(house.getMapId()));
        }
    }

    public static UserProgress fromString(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter().nullSafe())
                .create();
        return gson.fromJson(json, UserProgress.class);
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter().nullSafe())
                .create();
        return gson.toJson(this.progressPerHouseMap);
    }

    public long getTotalCurrentCoinsForHouses() {
        return progressPerHouseMap.values().stream()
                .mapToLong(HouseProgress::getCurrentCoins)
                .sum();
    }
    public long getTotalMaxCoinsForHouses() {
        return progressPerHouseMap.values().stream()
                .mapToLong(HouseProgress::getMaxCoins)
                .sum();
    }

    public HouseProgress getHouseProgressByHouseMapId(int mapId) {
        return progressPerHouseMap.values().stream()
                .filter(prog -> prog.getMapId() == mapId)
                .findAny()
                .orElse(null);
    }
}


