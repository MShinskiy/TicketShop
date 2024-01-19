package com.lavkatech.townofgames.entity;

import com.lavkatech.townofgames.entity.dto.HouseStatusDto;
import com.lavkatech.townofgames.entity.enums.Group;
import com.lavkatech.townofgames.entity.enums.TaskStatus;
import com.lavkatech.townofgames.entity.enums.VisibilityStatus;
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

    private String name;
    private String description;
    private String buttonText1;
    private String buttonURL1;
    private String buttonText2;
    private String buttonURL2;
    @Enumerated(EnumType.STRING)
    private VisibilityStatus visibilityStatus;
    // Принадлежность дома к карте,
    //дома с группой SIX всегда на карте домов с группой NINE
    @Enumerated(EnumType.STRING)
    private Group houseGroup;

    @OneToMany(mappedBy = "house")
    private List<HouseVisitLog> houseVisitLogList;

    @OneToOne
    @JoinColumn(name = "id")
    private Task task1;

    @OneToOne
    @JoinColumn(name = "id")
    private Task task2;

    /*@OneToMany(mappedBy = "house")
    private List<Task> houseTasks;*/

    // Получить DTO из статуса дома игрока
    public HouseStatusDto toDto(User user) {
        // Получить прогресс пользователя на поле
        UserProgress userProgress =
                UserProgress.fromString(user.getUserProgressJson());

        //Получить прогресс пользователя по дому
        TasksProgress houseProgress = userProgress
                .getProgressPerHouseMap()
                .get(id);

        //Получить данные о заданиях дома
        int tasksCompleted = houseProgress.completed();
        int tasksTotal = houseProgress.total();

        // Всего заданий 0? -> EMPTY, сделаны все задания? -> COMPLETE, иначе AVAILABLE
        TaskStatus status = tasksTotal > 0 ?
                tasksTotal == tasksCompleted ?
                        TaskStatus.COMPLETE : TaskStatus.AVAILABLE
                : TaskStatus.EMPTY;
        // Создание DTO
        HouseStatusDto dto
                = new HouseStatusDto(name, description, tasksCompleted, tasksTotal, status);

        // Добавление информации о заданиях
        for(Task task : houseProgress.keys())
            dto.addTask(task.getDescription(), houseProgress.get(task));

        // Добавление информации о кнопках
        dto.addButton(buttonText1, buttonURL1);
        dto.addButton(buttonText2, buttonURL2);

        return dto;
    }
}
