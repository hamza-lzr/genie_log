package com.example.miniprojet_genie_logiciel.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStoryDTO {
    private Long id;
    private String title;
    private String role;
    private String action;
    private String goal;
    private PriorityDTO priority;
    private StatusDTO status;
    private String acceptanceCriteria;
    private Integer storyPoints;
    private EpicDTO epic;
    private List<TaskDTO> tasks;
}