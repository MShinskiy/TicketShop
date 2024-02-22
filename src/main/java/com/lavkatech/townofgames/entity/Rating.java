package com.lavkatech.townofgames.entity;

import com.lavkatech.townofgames.entity.enums.Group;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "ratings")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private int coins;
    private String branch;
    @Enumerated(EnumType.STRING)
    private Group group;

    @ManyToOne
    @JoinColumn(referencedColumnName = "dtprf")
    private User user;
    //private String dtprf;

}
