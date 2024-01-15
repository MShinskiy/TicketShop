package com.lavkatech.townofgames.entity;

import com.lavkatech.townofgames.entity.cosnt.TaskStatus;
import com.lavkatech.townofgames.entity.cosnt.VisibilityStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

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

    private String description;
    private TaskStatus taskStatus;
    private VisibilityStatus visibilityStatus;
}
