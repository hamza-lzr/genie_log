package com.example.miniprojet_genie_logiciel.dto;

import com.example.miniprojet_genie_logiciel.entities.ProductBacklog;
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
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;


}