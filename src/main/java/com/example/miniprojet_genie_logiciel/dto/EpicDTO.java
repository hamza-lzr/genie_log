package com.example.miniprojet_genie_logiciel.dto;

import com.example.miniprojet_genie_logiciel.entities.Status;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpicDTO {
    private Long id;
    private String title;
    private String description;
    private String status;
    private List<UserStoryDTO> userStories;

}