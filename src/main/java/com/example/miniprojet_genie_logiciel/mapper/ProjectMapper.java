package com.example.miniprojet_genie_logiciel.mapper;

import com.example.miniprojet_genie_logiciel.dto.ProjectDTO;
import com.example.miniprojet_genie_logiciel.dto.ProductBacklogDTO;
import com.example.miniprojet_genie_logiciel.dto.EpicDTO;
import com.example.miniprojet_genie_logiciel.dto.SprintBacklogDTO;
import com.example.miniprojet_genie_logiciel.entities.Project;
import com.example.miniprojet_genie_logiciel.entities.ProductBacklog;
import com.example.miniprojet_genie_logiciel.entities.Epic;
import com.example.miniprojet_genie_logiciel.entities.SprintBacklog;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectMapper {

    public ProjectDTO toDto(Project project) {
        if (project == null) {
            return null;
        }

        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getTitle());
        dto.setDescription(project.getTitle());
        dto.setStatus(project.getStatus());
        dto.setStartDate(project.getStartDate() != null ? project.getStartDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate() : null);
        dto.setEndDate(project.getEndDate() != null ? project.getEndDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate() : null);
        
        if (project.getProductBacklog() != null) {
            ProductBacklogDTO productBacklogDTO = new ProductBacklogDTO();
            productBacklogDTO.setId(project.getProductBacklog().getId());
            productBacklogDTO.setName(project.getProductBacklog().getName());
            dto.setProductBacklog(productBacklogDTO);
        }

        return dto;
    }

    public Project toEntity(ProjectDTO dto) {
        if (dto == null) {
            return null;
        }

        Project project = new Project();
        project.setId(dto.getId());
        project.setTitle(dto.getName());
        project.setStatus(dto.getStatus());
        project.setStartDate(dto.getStartDate() != null ? java.util.Date.from(dto.getStartDate().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()) : null);
        project.setEndDate(dto.getEndDate() != null ? java.util.Date.from(dto.getEndDate().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()) : null);
        
        if (dto.getProductBacklog() != null) {
            ProductBacklog productBacklog = new ProductBacklog();
            productBacklog.setId(dto.getProductBacklog().getId());
            productBacklog.setName(dto.getProductBacklog().getName());
            project.setProductBacklog(productBacklog);
        }

        return project;
    }
}