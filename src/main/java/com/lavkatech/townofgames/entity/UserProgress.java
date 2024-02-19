package com.lavkatech.townofgames.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lavkatech.townofgames.misc.LocalDateTimeAdapter;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    public static UserProgress fromString(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter().nullSafe())
                .create();
        List<HouseProgress> progresses = new ArrayList<>(List.of(gson.fromJson(json, HouseProgress[].class)));
        return new UserProgress(progresses);
    }

    public HouseProgress getHouseProgressByHouseMapId(int mapId) {
        return progressPerHouseList.stream()
                .filter(prog -> prog.getMapId() == mapId)
                .findAny()
                .orElseGet(() -> createHouse(mapId));
    }

    public void updateHouses(List<House> houses) {
        for (House house : houses) {
            HouseProgress progress = progressPerHouseList.stream()
                    .filter(prog -> prog.getMapId() == house.getMapId())
                    .findAny()
                    .orElseGet(() -> createHouse(house.getMapId()));

            progress.setTaskDesc1(house.getTask1());
            progress.setTaskDesc2(house.getTask2());
        }
    }

    private HouseProgress createHouse(int mapId) {
        HouseProgress hp = new HouseProgress(mapId);
        progressPerHouseList.add(hp);
        return hp;
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

    @Override
    public String toString() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter().nullSafe())
                .create();
        return gson.toJson(this.progressPerHouseList);
    }
        /*public static String initString(List<House> houses) {
        UserProgress userProgress = new UserProgress();
        for(House house : houses)
            userProgress.getProgressPerHouseList()
                    .add(new HouseProgress(house.getId(), house.getMapId()));

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter().nullSafe())
                .create();
        return gson.toJson(userProgress.progressPerHouseList);
    }*/

    /*public void addNewHouses(List<House> houses) {
        for(House house : houses) {
            if(progressPerHouseList.stream()
                    .noneMatch(h -> h.getHouseId().equals(house.getId())))
                progressPerHouseList.add(new HouseProgress(house.getId(), house.getMapId()));
        }
    }

*/
}


