package com.example.miniprojet_genie_logiciel.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductBacklogDTO {
    private Long id;
    private String name;
    private String description;
    private ProjectDTO project;
    private List<UserStoryDTO> userStories;

}