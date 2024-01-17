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
@Table(name = "usersTasks")
@AllArgsConstructor
@NoArgsConstructor
public class UserTask {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "dtprf")
    private User user;

    @OneToOne
    private Task task;
}
*/
