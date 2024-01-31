package com.lavkatech.townofgames.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class HouseProgress {
/*    //Task - isComplete
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final Map<String, Boolean> tasksStatus;
    //order - task
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final Map<Integer, String> tasksOrder;*/
    private final UUID houseId;
    private final int mapId;
    private long maxCoins = 0;
    private long currentCoins = 0;
    private String descVar1 = "";
    private String descVar2 = "";
    private String descVar3 = "";
    private String taskDesc1 = "";
    private boolean taskStatus1 = false;
    private String taskDesc2 = "";
    private boolean taskStatus2 = false;
    private String visitTimeStamp = "";

    public HouseProgress(UUID houseId, int mapId) {
        this.houseId = houseId;
        this.mapId = mapId;
        /*tasksStatus = new LinkedHashMap<>();
        tasksOrder = new LinkedHashMap<>();*/
    }

    // Вернуть кол-во всех заданий
    public int tasksTotal() {
        int count = 0;
        if(!taskDesc1.isEmpty()) count++;
        if(!taskDesc2.isEmpty()) count++;
        return count;
    }

    // Вернуть кол-во завершенных заданий
    public int tasksCompleted() {
        int count = 0;
        if(taskStatus1) count++;
        if(taskStatus2) count++;
        return count;
    }

    /*// Добавить задание
    public void putTask(String task) {
        tasksStatus.put(task, false);
    }

    public void putTask(String task, int order) {
        tasksStatus.put(task, false);
        tasksOrder.put(order, task);
    }

    public void removeTask(int order) {
        String taskUUID = tasksOrder.get(order);
        tasksStatus.remove(taskUUID);
        tasksOrder.remove(order);
    }

    // Вернуть статус задания
    public Boolean getTaskStatus(String task) {
        return tasksStatus.get(task);
    }

    public String getTaskByOrder(int order){
        return tasksOrder.get(order);
    }

    // Вернуть задания
    public List<String> getTasksList() {
        return new ArrayList<>(tasksStatus.keySet());
    }

*//*    public boolean hasTaskCode(int taskCode) {
        for (UUID taskId : getTasksList()) {

        }
    }*//*

    //return false if previous mapping was true (was already marked as finished) or no mapping/null (doesn't exist)
    //true - complete, false - active
    public boolean finishTask(String task) {
        Boolean res = tasksStatus.put(task, true);
        return res != null && !res;
    }*/
}
