package com.example.miniprojet_genie_logiciel.dto;

import com.example.miniprojet_genie_logiciel.entities.Status;
import lombok.Data;

@Data
public class UpdateEpicDTO {
    private String title;
    private String description;
    private Status status;
}

