package com.example.miniprojet_genie_logiciel.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private ProductBacklogDTO productBacklog;
    private List<EpicDTO> epics;
    private List<SprintBacklogDTO> sprintBacklogs;

}