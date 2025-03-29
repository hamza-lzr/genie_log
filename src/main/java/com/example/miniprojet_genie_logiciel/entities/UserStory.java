package com.example.miniprojet_genie_logiciel.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table
@Data
public class UserStory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String role;

    @Column
    private String action;

    @Column
    private String goal;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    private Epic epic;

    @ManyToOne
    private ProductBacklog productBacklog;

    @ManyToOne
    private SprintBacklog sprintBacklog;

    @OneToMany(mappedBy = "userStory")
    private List<Task> tasks;

    @Column
    @Enumerated(EnumType.STRING) // Stocke "Must Have", "Should Have", etc. en base
    private Priority priority;

    @Column
    private int priorityOrder;

    @Column
    private String acceptanceCriteria;


}
