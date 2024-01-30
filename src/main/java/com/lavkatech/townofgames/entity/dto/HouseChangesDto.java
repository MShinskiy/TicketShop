package com.lavkatech.townofgames.entity.dto;

import com.google.gson.Gson;
import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import com.lavkatech.townofgames.entity.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
public class HouseChangesDto {
    private List<HouseEdit> edits;


    public HouseChangesDto(String editsJson) {
        Gson gson = new Gson();
        this.edits = List.of(gson.fromJson(editsJson, HouseEdit[].class));
    }
    public MapDto toDemoMapDto() {
        Map<Integer, HouseStatusDto> map = new HashMap<>();
        int totalTasks = 0, completeTasks = 0;
        String group = "", level = "";
        for(HouseEdit e : edits) {
            group = e.getGroup();
            level = e.getLevel();
            int tt = 0, tc = 0;
            String task1 = null, task2 = null;
            String text1 = null, text2 = null, text3 = null;
            String url1 = null, url2 = null, url3 = null;
            if(!Objects.equals(e.getTask1(), "")) {
                task1 = e.getTask1();
                tt++;
                totalTasks++;
                tc++;
                completeTasks++;
            }
            if(!Objects.equals(e.getTask2(), "")) {
                task2 = e.getTask2();
                tt++;
                totalTasks++;
                tc++;
                completeTasks++;
            }
            if(!Objects.equals(e.getText1(), ""))
                text1 = e.getText1();
            if(!Objects.equals(e.getText2(), ""))
                text2 = e.getText2();
            if(!Objects.equals(e.getText3(), ""))
                text3 = e.getText3();
            if(!Objects.equals(e.getUrl1(), ""))
                url1 = e.getUrl1();
            if(!Objects.equals(e.getUrl2(), ""))
                url2 = e.getUrl2();
            if(!Objects.equals(e.getUrl3(), ""))
                url3 = e.getUrl3();

            TaskStatus ts = tt > 0? TaskStatus.AVAILABLE : TaskStatus.EMPTY;

            HouseStatusDto hsd = new HouseStatusDto(e.getName(),
                    e.getDescription(),
                    tc,
                    tt,
                    ts,
                    1000,
                    e.getProgress(),
                    e.getCaption());

            if(task1 != null)
                hsd.addTask(task1, false);
            if(task2 != null)
                hsd.addTask(task2, false);
            hsd.addButton(text1, url1);
            hsd.addButton(text2, url2);
            hsd.addButton(text3, url3);
            map.put(Integer.valueOf(e.getMapId()), hsd);
        }
        UserDto userDto = UserDto.builder()
                .dtprf("DEMO")
                .username("demo player")
                .coins(1000)
                .maxCoins(5000)
                .group(Group.valueOf(group))
                .level(LevelSA.valueOf(level))
                .points(2000)
                .tasksCount(completeTasks)
                .tasksTotal(totalTasks)
                .build();

        return new MapDto(userDto, map);
    }
}

