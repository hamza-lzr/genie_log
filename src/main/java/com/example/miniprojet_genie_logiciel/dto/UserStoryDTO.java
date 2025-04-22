package com.example.miniprojet_genie_logiciel.dto;

import com.example.miniprojet_genie_logiciel.entities.Priority;
import com.example.miniprojet_genie_logiciel.entities.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserStoryDTO {
    private Long id;
    private String title;
    private Integer storyPoints;
    private Status status;
    private Priority priority;
    private String acceptanceCriteria;
    private Long epicId;
    private Long productBacklogId;
    private Long sprintBacklogId;
}

