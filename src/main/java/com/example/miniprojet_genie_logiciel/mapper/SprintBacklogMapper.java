package com.example.miniprojet_genie_logiciel.mapper;

import com.example.miniprojet_genie_logiciel.dto.CreateSprintBacklogDTO;
import com.example.miniprojet_genie_logiciel.dto.SprintBacklogDTO;
import com.example.miniprojet_genie_logiciel.dto.UserStoryDTO;
import com.example.miniprojet_genie_logiciel.entities.SprintBacklog;
import com.example.miniprojet_genie_logiciel.entities.UserStory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SprintBacklogMapper {

    public SprintBacklogDTO toDto(SprintBacklog sprintBacklog) {
        if (sprintBacklog == null) return null;

        SprintBacklogDTO dto = new SprintBacklogDTO();
        dto.setId(sprintBacklog.getId());
        dto.setName(sprintBacklog.getName());
        dto.setStartDate(sprintBacklog.getStartDate());
        dto.setEndDate(sprintBacklog.getEndDate());
        dto.setStatus(sprintBacklog.getStatus());

        if (sprintBacklog.getUserStories() != null) {
            List<UserStoryDTO> userStoryDTOs = sprintBacklog.getUserStories().stream()
                    .map(this::mapUserStoryToDTO)
                    .collect(Collectors.toList());
            dto.setUserStories(userStoryDTOs);
        }

        return dto;
    }

    public SprintBacklog toEntity(CreateSprintBacklogDTO dto) {
        if (dto == null) return null;

        SprintBacklog sprintBacklog = new SprintBacklog();
        sprintBacklog.setName(dto.getName());
        sprintBacklog.setStartDate(dto.getStartDate());
        sprintBacklog.setEndDate(dto.getEndDate());
        sprintBacklog.setStatus(dto.getStatus());
        return sprintBacklog;
    }

    private UserStoryDTO mapUserStoryToDTO(UserStory userStory) {
        if (userStory == null) return null;

        UserStoryDTO dto = new UserStoryDTO();
        dto.setId(userStory.getId());
        dto.setTitle(userStory.getTitle());
        dto.setStatus(userStory.getStatus());
        return dto;
    }
}

