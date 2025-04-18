package com.example.miniprojet_genie_logiciel.mapper;

import com.example.miniprojet_genie_logiciel.dto.EpicDTO;
import com.example.miniprojet_genie_logiciel.dto.UserStoryDTO;
import com.example.miniprojet_genie_logiciel.entities.Epic;
import com.example.miniprojet_genie_logiciel.entities.UserStory;
import com.example.miniprojet_genie_logiciel.entities.Status;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EpicMapper {

    public EpicDTO toDto(Epic epic) {
        if (epic == null) {
            return null;
        }

        EpicDTO epicDTO = new EpicDTO();
        epicDTO.setId(epic.getId());
        epicDTO.setTitle(epic.getTitle());
        epicDTO.setDescription(epic.getDescription());
        epicDTO.setStatus(epic.getStatus().toString());
        
        if (epic.getUserStories() != null) {
            List<UserStoryDTO> userStoryDTOs = epic.getUserStories().stream()
                .map(this::mapUserStoryToDTO)
                .collect(Collectors.toList());
            epicDTO.setUserStories(userStoryDTOs);
        }

        return epicDTO;
    }

    public Epic toEntity(EpicDTO epicDTO) {
        if (epicDTO == null) {
            return null;
        }

        Epic epic = new Epic();
        epic.setId(epicDTO.getId());
        epic.setTitle(epicDTO.getTitle());
        epic.setDescription(epicDTO.getDescription());
        epic.setStatus(Status.valueOf(epicDTO.getStatus()));

        if (epicDTO.getUserStories() != null) {
            List<UserStory> userStories = epicDTO.getUserStories().stream()
                .map(this::mapDTOToUserStory)
                .collect(Collectors.toList());
            epic.setUserStories(userStories);
        }

        return epic;
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

