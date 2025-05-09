package com.example.miniprojet_genie_logiciel.dto;

import com.example.miniprojet_genie_logiciel.entities.Status;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SprintBacklogDTO {
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Status status;
    private Long projectId;
    private List<UserStoryDTO> userStories;
}
