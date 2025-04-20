package com.example.miniprojet_genie_logiciel.dto;

import com.example.miniprojet_genie_logiciel.entities.Status;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTaskDTO {
    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Status status;

    @Min(1)
    private int estimation;

    @NotNull
    private Long userStoryId;
}

