package com.example.miniprojet_genie_logiciel.dto;

import com.example.miniprojet_genie_logiciel.entities.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateSprintBacklogDTO {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long projectId;
    private Status status;
}

