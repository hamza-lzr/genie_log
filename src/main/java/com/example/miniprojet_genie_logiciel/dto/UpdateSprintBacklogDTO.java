package com.example.miniprojet_genie_logiciel.dto;

import com.example.miniprojet_genie_logiciel.entities.Status;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateSprintBacklogDTO {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Status status;
    private List<Long> userStoryIds; // pour ajouter ou retirer des user stories dans le sprint
}
