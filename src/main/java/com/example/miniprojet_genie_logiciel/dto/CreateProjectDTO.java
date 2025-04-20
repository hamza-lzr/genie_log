package com.example.miniprojet_genie_logiciel.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class CreateProjectDTO {
    private String name;
    private String description;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;

}

