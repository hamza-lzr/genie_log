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
    private String value;

    @Column
    private String priority;

    @Column
    private String status;

    @ManyToOne
    private Epic epic;

    @ManyToOne
    private ProductBacklog productBacklog;

    @ManyToOne
    private SprintBacklog sprintBacklog;

    @OneToMany(mappedBy = "userStory")
    private List<Task> tasks;

}
