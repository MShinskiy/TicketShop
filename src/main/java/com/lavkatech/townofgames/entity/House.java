package com.lavkatech.townofgames.entity;

import com.lavkatech.townofgames.entity.dto.HouseStatusDto;
import com.lavkatech.townofgames.entity.enums.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String name;
    private String description;
    private String buttonText1;
    private String buttonURL1;
    private String buttonText2;
    private String buttonURL2;
    private String buttonText3;
    private String buttonURL3;
    private String taskProgressDescription;
    @Enumerated(EnumType.STRING)
    private VisibilityStatus visibilityStatus;
    // Принадлежность дома к карте
    @Enumerated(EnumType.STRING)
    private Group houseGroup;
    // Принадлежность дома к уровню
    @Enumerated(EnumType.STRING)
    private LevelSA houseLevel;

    @OneToMany(mappedBy = "house")
    private List<HouseVisitLog> houseVisitLogList;

    @OneToOne
    @JoinColumn(name = "id")
    private Task task1 = null;

    @OneToOne
    @JoinColumn(name = "id")
    private Task task2 = null;

    /*@OneToMany(mappedBy = "house")
    private List<Task> houseTasks;*/

    // Получить DTO из статуса дома игрока
    public HouseStatusDto toDto(User user) {
        // Получить прогресс пользователя на поле
        UserProgress userProgress =
                UserProgress.fromString(user.getUserProgressJson());

        //Получить прогресс пользователя по дому
        HouseProgress houseProgress = userProgress
                .getProgressPerHouseMap()
                .get(id);

        //Получить данные о заданиях дома
        int tasksCompleted = houseProgress.tasksCompleted();
        int tasksTotal = houseProgress.tasksTotal();
        String renderedString = String.format(taskProgressDescription, new String[]{
                houseProgress.getDescVar1(),
                houseProgress.getDescVar2(),
                houseProgress.getDescVar3(),
        });
        long coins = houseProgress.getCurrentCoins();
        long maxCoins = houseProgress.getMaxCoins();

        // Всего заданий 0? -> EMPTY, сделаны все задания? -> COMPLETE, иначе AVAILABLE
        TaskStatus status = tasksTotal > 0 ?
                tasksTotal == tasksCompleted ?
                        TaskStatus.COMPLETE : TaskStatus.AVAILABLE
                : TaskStatus.EMPTY;
        // Создание DTO
        HouseStatusDto dto
                = new HouseStatusDto(name, description, tasksCompleted, tasksTotal, status, coins, maxCoins, renderedString);

        // Добавление информации о заданиях
        for(Task task : houseProgress.tasksList())
            dto.addTask(task.getDescription(), houseProgress.getTaskStatus(task));

        // Добавление информации о кнопках
        dto.addButton(buttonText1, buttonURL1);
        dto.addButton(buttonText2, buttonURL2);

        return dto;
    }
}
