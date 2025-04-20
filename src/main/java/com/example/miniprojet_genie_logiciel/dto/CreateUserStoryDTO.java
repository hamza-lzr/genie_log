package com.example.miniprojet_genie_logiciel.dto;

import com.example.miniprojet_genie_logiciel.entities.Priority;
import com.example.miniprojet_genie_logiciel.entities.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateUserStoryDTO {
    @NotBlank
    private String title;

    @NotNull
    private Integer storyPoints;

    @NotNull
    private Status status;

    @NotNull
    private Priority priority;

    private String acceptanceCriteria;

    @NotNull
    private Long epicId;

    @NotNull
    private Long productBacklogId;
}

