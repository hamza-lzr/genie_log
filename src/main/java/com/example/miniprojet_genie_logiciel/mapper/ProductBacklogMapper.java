package com.example.miniprojet_genie_logiciel.mapper;

import com.example.miniprojet_genie_logiciel.dto.ProductBacklogDTO;
import com.example.miniprojet_genie_logiciel.dto.ProjectDTO;
import com.example.miniprojet_genie_logiciel.dto.UserStoryDTO;
import com.example.miniprojet_genie_logiciel.entities.ProductBacklog;
import com.example.miniprojet_genie_logiciel.entities.Project;
import com.example.miniprojet_genie_logiciel.entities.UserStory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductBacklogMapper {

    public ProductBacklogDTO toDto(ProductBacklog productBacklog) {
        if (productBacklog == null) {
            return null;
        }

        ProductBacklogDTO dto = new ProductBacklogDTO();
        dto.setId(productBacklog.getId());
        dto.setName(productBacklog.getName());
        dto.setDescription(productBacklog.getDescription());

        if (productBacklog.getProject() != null) {
            ProjectDTO projectDTO = new ProjectDTO();
            projectDTO.setId(productBacklog.getProject().getId());
            projectDTO.setName(productBacklog.getProject().getTitle());
            dto.setProject(projectDTO);
        }

        if (productBacklog.getUserStories() != null) {
            List<UserStoryDTO> userStoryDTOs = productBacklog.getUserStories().stream()
                .map(this::mapUserStoryToDTO)
                .collect(Collectors.toList());
            dto.setUserStories(userStoryDTOs);
        }

        return dto;
    }

    public ProductBacklog toEntity(ProductBacklogDTO dto) {
        if (dto == null) {
            return null;
        }

        ProductBacklog productBacklog = new ProductBacklog();
        productBacklog.setId(dto.getId());
        productBacklog.setName(dto.getName());
        productBacklog.setDescription(dto.getDescription());

        if (dto.getProject() != null) {
            Project project = new Project();
            project.setId(dto.getProject().getId());
            project.setTitle(dto.getProject().getName());
            productBacklog.setProject(project);
        }

        if (dto.getUserStories() != null) {
            List<UserStory> userStories = dto.getUserStories().stream()
                .map(this::mapDTOToUserStory)
                .collect(Collectors.toList());
            productBacklog.setUserStories(userStories);
        }

        return productBacklog;
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


