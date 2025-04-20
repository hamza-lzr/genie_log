package com.example.miniprojet_genie_logiciel.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;


import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "sprint_backlog")
public class SprintBacklog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDate startDate;
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    private Status status; // Utilise la même enum "Status" que UserStory et Task

    // ⚠️ Les UserStories assignées à ce sprint (elles peuvent aussi exister dans le ProductBacklog)
    @OneToMany(mappedBy = "sprintBacklog", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserStory> userStories;

    // ⚠️ Les tâches ne sont jamais directement liées au SprintBacklog mais indirectement via UserStories
    @Transient
    public List<Task> getAllTasks() {
        return userStories == null ? List.of()
                : userStories.stream()
                .flatMap(us -> us.getTasks().stream())
                .toList();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;

}



