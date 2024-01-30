package com.lavkatech.townofgames.entity;

import com.lavkatech.townofgames.entity.enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Сущность описывает дома которые будут отображены на карте
 */
@Entity
@Table(name = "houses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int mapId;
    private String name = "";
    private String description = "";
    private String buttonText1 = "";
    private String buttonURL1 = "";
    private String buttonText2 = "";
    private String buttonURL2 = "";
    private String buttonText3 = "";
    private String buttonURL3 = "";
    private String taskProgressDescription = "{1}{2}{3}";
    private String caption = "";

    @Enumerated(EnumType.STRING)
    private VisibilityStatus visibilityStatus = VisibilityStatus.VISIBLE;
    // Принадлежность дома к карте
    @Enumerated(EnumType.STRING)
    private Group houseGroup;
    // Принадлежность дома к уровню
    @Enumerated(EnumType.STRING)
    private LevelSA houseLevel;

    @OneToMany(mappedBy = "house")
    private List<HouseVisitLog> houseVisitLogList = new ArrayList<>();

    /*@OneToOne
    @JoinColumn(name = "id")
    private Task task1 = null;

    @OneToOne
    @JoinColumn(name = "id")
    private Task task2 = null;*/

    public House(int mapId, String name, Group houseGroup, LevelSA houseLevel) {
        this.mapId = mapId;
        this.name = name;
        this.houseGroup = houseGroup;
        this.houseLevel = houseLevel;
    }

    @OneToMany(mappedBy = "house")
    private List<Task> houseTasks;

    public Task getTask1(){
        return houseTasks.stream()
                .filter(t -> t.getTaskOrder() == 1)
                .findAny()
                .orElse(null);
    }

    public void replaceTask(Task newTask) {
        houseTasks.replaceAll(oldTask ->
                oldTask.getTaskOrder() == newTask.getTaskOrder() ?
                        newTask:
                        oldTask
        );
    }

    public Task getTask2() {
        return houseTasks.stream()
                .filter(t -> t.getTaskOrder() == 2)
                .findAny()
                .orElse(null);
    }
}
