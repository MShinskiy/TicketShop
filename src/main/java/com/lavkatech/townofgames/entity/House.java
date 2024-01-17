package com.lavkatech.townofgames.entity;

import com.lavkatech.townofgames.entity.cosnt.Group;
import com.lavkatech.townofgames.entity.cosnt.TaskStatus;
import com.lavkatech.townofgames.entity.cosnt.VisibilityStatus;
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
    private VisibilityStatus visibilityStatus;
    private Group houseGroup;   //Принадлежность дома к карте

    @OneToOne
    @JoinColumn(name = "id")
    private Task task1;

    @OneToOne
    @JoinColumn(name = "id")
    private Task task2;

    /*@OneToMany(mappedBy = "house")
    private List<Task> houseTasks;*/
}
