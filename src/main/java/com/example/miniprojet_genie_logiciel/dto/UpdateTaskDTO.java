package com.example.miniprojet_genie_logiciel.dto;

import com.example.miniprojet_genie_logiciel.entities.Status;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class UpdateTaskDTO {
    private String title;
    private String description;
    private Status status;

    @Min(1)
    private Integer estimation;
}

