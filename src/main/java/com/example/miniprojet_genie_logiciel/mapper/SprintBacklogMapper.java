package com.example.miniprojet_genie_logiciel.mapper;

import com.example.miniprojet_genie_logiciel.dto.SprintBacklogDTO;
import com.example.miniprojet_genie_logiciel.dto.UserStoryDTO;
import com.example.miniprojet_genie_logiciel.dto.TaskDTO;
import com.example.miniprojet_genie_logiciel.entities.SprintBacklog;
import com.example.miniprojet_genie_logiciel.entities.UserStory;
import com.example.miniprojet_genie_logiciel.entities.Task;
import com.example.miniprojet_genie_logiciel.entities.Status;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SprintBacklogMapper {

    public SprintBacklogDTO toDto(SprintBacklog sprintBacklog) {
        if (sprintBacklog == null) {
            return null;
        }

        SprintBacklogDTO dto = new SprintBacklogDTO();
        dto.setId(sprintBacklog.getId());
        dto.setName(sprintBacklog.getName());
        
        if (sprintBacklog.getUserStories() != null) {
            List<UserStoryDTO> userStoryDTOs = sprintBacklog.getUserStories().stream()
                .map(this::mapUserStoryToDTO)
                .collect(Collectors.toList());
            dto.setUserStories(userStoryDTOs);
        }

        return dto;
    }

    public SprintBacklog toEntity(SprintBacklogDTO dto) {
        if (dto == null) {
            return null;
        }

        SprintBacklog sprintBacklog = new SprintBacklog();
        sprintBacklog.setId(dto.getId());
        sprintBacklog.setName(dto.getName());

        if (dto.getUserStories() != null) {
            List<UserStory> userStories = dto.getUserStories().stream()
                .map(this::mapDTOToUserStory)
                .collect(Collectors.toList());
            sprintBacklog.setUserStories(userStories);
        }

        return sprintBacklog;
    }

    private UserStoryDTO mapUserStoryToDTO(UserStory userStory) {
        if (userStory == null) {
            return null;
        }
        UserStoryDTO dto = new UserStoryDTO();
        dto.setId(userStory.getId());
        dto.setTitle(userStory.getTitle());
        return dto;
    }

    private UserStory mapDTOToUserStory(UserStoryDTO dto) {
        if (dto == null) {
            return null;
        }
        UserStory userStory = new UserStory();
        userStory.setId(dto.getId());
        userStory.setTitle(dto.getTitle());
        return userStory;
    }
}

