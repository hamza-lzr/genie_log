package com.example.miniprojet_genie_logiciel.mapper;

import com.example.miniprojet_genie_logiciel.dto.CreateUserStoryDTO;
import com.example.miniprojet_genie_logiciel.dto.UpdateUserStoryDTO;
import com.example.miniprojet_genie_logiciel.dto.UserStoryDTO;
import com.example.miniprojet_genie_logiciel.entities.Epic;
import com.example.miniprojet_genie_logiciel.entities.ProductBacklog;
import com.example.miniprojet_genie_logiciel.entities.SprintBacklog;
import com.example.miniprojet_genie_logiciel.entities.UserStory;
import org.springframework.stereotype.Component;

@Component
public class UserStoryMapper {

    public UserStoryDTO toDto(UserStory userStory) {
        if (userStory == null) return null;

        UserStoryDTO dto = new UserStoryDTO();
        dto.setId(userStory.getId());
        dto.setTitle(userStory.getTitle());
        dto.setStoryPoints(userStory.getStoryPoints());
        dto.setStatus(userStory.getStatus());
        dto.setPriority(userStory.getPriority());
        dto.setAcceptanceCriteria(userStory.getAcceptanceCriteria());

        if (userStory.getEpic() != null)
            dto.setEpicId(userStory.getEpic().getId());

        if (userStory.getProductBacklog() != null)
            dto.setProductBacklogId(userStory.getProductBacklog().getId());

        if (userStory.getSprintBacklog() != null)
            dto.setSprintBacklogId(userStory.getSprintBacklog().getId());

        return dto;
    }

    public UserStory fromCreateDto(CreateUserStoryDTO dto, Epic epic, ProductBacklog productBacklog) {
        UserStory userStory = new UserStory();
        userStory.setTitle(dto.getTitle());
        userStory.setStoryPoints(dto.getStoryPoints());
        userStory.setStatus(dto.getStatus());
        userStory.setPriority(dto.getPriority());
        userStory.setAcceptanceCriteria(dto.getAcceptanceCriteria());
        userStory.setEpic(epic);
        userStory.setProductBacklog(productBacklog);
        return userStory;
    }

    public void updateFromDto(UpdateUserStoryDTO dto, UserStory userStory, SprintBacklog sprintBacklog) {
        if (dto.getTitle() != null)
            userStory.setTitle(dto.getTitle());

        if (dto.getStoryPoints() != null)
            userStory.setStoryPoints(dto.getStoryPoints());

        if (dto.getStatus() != null)
            userStory.setStatus(dto.getStatus());

        if (dto.getPriority() != null)
            userStory.setPriority(dto.getPriority());

        if (dto.getAcceptanceCriteria() != null)
            userStory.setAcceptanceCriteria(dto.getAcceptanceCriteria());

        if (sprintBacklog != null)
            userStory.setSprintBacklog(sprintBacklog);
    }
}
