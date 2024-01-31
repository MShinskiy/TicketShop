/*
package com.lavkatech.townofgames.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Builder
@Table(name = "tasks")
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private int taskOrder = -1;
    @Column(length = 300)
    private String description = "";
    //private long reward = 0;

    @ManyToOne
    private House house = null;

    public Task(int taskOrder, String description, House house) {
        this.taskOrder = taskOrder;
        this.description = description;
        this.house = house;
    }
}
*/
