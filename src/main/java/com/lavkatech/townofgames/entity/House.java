package com.lavkatech.townofgames.entity;

import com.lavkatech.townofgames.entity.enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Сущность описывает дома которые будут отображены на карте
 */
@Entity
@Table(name = "houses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class House {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int mapId;
    private String name = "";
    @Column(length = 300)
    private String description = "";
    private String buttonText1 = "";
    private String buttonURL1 = "";
    private String buttonText2 = "";
    private String buttonURL2 = "";
    private String buttonText3 = "";
    private String buttonURL3 = "";
    private String taskProgressDescription = "{1}{2}{3}";
    @Column(length = 300)
    private String caption = "";

    @Enumerated(EnumType.STRING)
    private VisibilityStatus visibilityStatus = VisibilityStatus.VISIBLE;
    // Принадлежность дома к карте
    @Enumerated(EnumType.STRING)
    private Group houseGroup;
    // Принадлежность дома к уровню
    @Enumerated(EnumType.STRING)
    private LevelSA houseLevel;
    @Column(length = 300)
    private String task1 = "";
    @Column(length = 300)
    private String task2 = "";

    public House(int mapId, String name, Group houseGroup, LevelSA houseLevel) {
        this.mapId = mapId;
        this.name = name;
        this.houseGroup = houseGroup;
        this.houseLevel = houseLevel;
    }

    /*public Task getTask1() {
        return houseTasks.stream()
                .filter(t -> t.getTaskOrder() == 1)
                .findAny()
                .orElse(null);
    }

    public Task getTask2() {
        return houseTasks.stream()
                .filter(t -> t.getTaskOrder() == 2)
                .findAny()
                .orElse(null);
    }*/

    /*public UUID deleteTaskWithOrder(int order) {
        Optional<Task> optTask = houseTasks.stream()
                .filter(task -> task.getTaskOrder() == order)
                .findAny();

        optTask.ifPresent(oldTask -> houseTasks.remove(oldTask));
        return optTask.orElse(null) != null? optTask.get().getId() : null;
    }

    public UUID replaceTask(Task newTask) {
        UUID uuid = deleteTaskWithOrder(newTask.getTaskOrder());
        houseTasks.add(newTask);
        return uuid;
    }

    public boolean containsAtOrder(int order) {
        return houseTasks.stream()
                .anyMatch(task -> task.getTaskOrder() == order);
    }*/
}
