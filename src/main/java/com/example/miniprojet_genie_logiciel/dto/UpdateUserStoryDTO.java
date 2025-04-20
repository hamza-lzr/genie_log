package com.example.miniprojet_genie_logiciel.dto;

import com.example.miniprojet_genie_logiciel.entities.Priority;
import com.example.miniprojet_genie_logiciel.entities.Status;
import lombok.Data;

@Data
public class UpdateUserStoryDTO {
    private String title;
    private Integer storyPoints;
    private Status status;
    private Priority priority;
    private String acceptanceCriteria;
    private Long sprintBacklogId; // optionnel si assignation plus tard
}

