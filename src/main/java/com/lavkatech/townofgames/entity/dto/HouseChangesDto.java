package com.lavkatech.townofgames.entity.dto;

import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.enums.LevelSA;
import com.lavkatech.townofgames.entity.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public class HouseChangesDto {
    private String level;
    private String group;
    private Map<UUID, HouseEdit> editMap = new HashMap<>();

    public MapDto toMapDto() {
        Map<Integer, HouseStatusDto> map = new HashMap<>();
        int totalTasks = 0, completeTasks = 0;
        for(HouseEdit e : editMap.values()) {
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
                url1 = e.getText1();
            if(!Objects.equals(e.getUrl2(), ""))
                url2 = e.getText2();
            if(!Objects.equals(e.getUrl3(), ""))
                url3 = e.getText3();

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
            if(text1 != null && url1 != null)
                hsd.addButton(text1, url1);
            if(text2 != null && url2 != null)
                hsd.addButton(text2, url2);
            if(text3 != null && url3 != null)
                hsd.addButton(text3, url3);
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

