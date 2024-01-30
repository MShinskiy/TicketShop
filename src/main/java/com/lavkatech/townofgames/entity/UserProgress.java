package com.lavkatech.townofgames.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lavkatech.townofgames.misc.LocalDateTimeAdapter;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class UserProgress {
    //House id - task progress
    private final List<HouseProgress> progressPerHouseList;

    public UserProgress() {
        this.progressPerHouseList = new ArrayList<>();
    }

    public UserProgress(List<HouseProgress> progress) {
        this.progressPerHouseList = progress;
    }


    public static String initString() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter().nullSafe())
                .create();
        return gson.toJson(new UserProgress().progressPerHouseList);
    }

    public static String initString(List<House> houses) {
        UserProgress userProgress = new UserProgress();
        for(House house : houses)
            userProgress.getProgressPerHouseList()
                    .add(new HouseProgress(house.getId(), house.getMapId()));

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter().nullSafe())
                .create();
        return gson.toJson(userProgress.progressPerHouseList);
    }

    public void addNewHouses(List<House> houses) {
        for(House house : houses) {
            if(progressPerHouseList.stream()
                    .noneMatch(h -> h.getHouseId().equals(house.getId())))
                progressPerHouseList.add(new HouseProgress(house.getId(), house.getMapId()));
        }
    }

    public static UserProgress fromString(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter().nullSafe())
                .create();
        List<HouseProgress> progresses = new ArrayList<>(List.of(gson.fromJson(json, HouseProgress[].class)));
        return new UserProgress(progresses);
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter().nullSafe())
                .create();
        return gson.toJson(this.progressPerHouseList);
    }

    public long getTotalCurrentCoinsForHouses() {
        return progressPerHouseList.stream()
                .mapToLong(HouseProgress::getCurrentCoins)
                .sum();
    }
    public long getTotalMaxCoinsForHouses() {
        return progressPerHouseList.stream()
                .mapToLong(HouseProgress::getMaxCoins)
                .sum();
    }

    public HouseProgress getHouseProgressByHouseMapId(int mapId) {
        return progressPerHouseList.stream()
                .filter(prog -> prog.getMapId() == mapId)
                .findAny()
                .orElse(null);
    }

    public HouseProgress getHouseProgressByHouseId(UUID houseId) {
        return progressPerHouseList.stream()
                .filter(prog -> prog.getHouseId().equals(houseId))
                .findAny()
                .orElse(null);
    }
}


