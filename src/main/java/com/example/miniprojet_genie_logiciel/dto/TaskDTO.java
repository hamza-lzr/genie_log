package com.example.miniprojet_genie_logiciel.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private StatusDTO status;
    private Integer estimation;
    private UserStoryDTO userStory;
}