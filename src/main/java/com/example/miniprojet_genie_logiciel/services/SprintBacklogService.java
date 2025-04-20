package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.dto.CreateSprintBacklogDTO;
import com.example.miniprojet_genie_logiciel.dto.SprintBacklogDTO;
import com.example.miniprojet_genie_logiciel.dto.TaskDTO;
import com.example.miniprojet_genie_logiciel.entities.*;
import com.example.miniprojet_genie_logiciel.mapper.SprintBacklogMapper;
import com.example.miniprojet_genie_logiciel.mapper.TaskMapper;
import com.example.miniprojet_genie_logiciel.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SprintBacklogService {

    private final SprintBacklogRepository sprintRepo;
    private final ProjectRepository projectRepo;
    private final UserStoryRepository userStoryRepo;
    private final TaskRepository taskRepo;

    private final SprintBacklogMapper sprintMapper;
    private final TaskMapper taskMapper;

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER')")
    public SprintBacklogDTO createSprint(CreateSprintBacklogDTO dto) {
        Project project = projectRepo.findById(dto.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + dto.getProjectId()));

        SprintBacklog sprint = sprintMapper.toEntity(dto);
        sprint.setProject(project);

        SprintBacklog saved = sprintRepo.save(sprint);
        return sprintMapper.toDto(saved);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER')")
    public void deleteSprint(Long id) {
        if (!sprintRepo.existsById(id)) {
            throw new EntityNotFoundException("Sprint not found with id: " + id);
        }
        sprintRepo.deleteById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER')")
    public List<SprintBacklogDTO> getAllSprints() {
        return sprintRepo.findAll().stream()
                .map(sprintMapper::toDto)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER')")
    public SprintBacklogDTO getSprintById(Long id) {
        SprintBacklog sprint = sprintRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Sprint not found with id: " + id));
        return sprintMapper.toDto(sprint);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER')")
    public SprintBacklogDTO addUserStoryToSprint(Long sprintId, Long userStoryId) {
        SprintBacklog sprint = sprintRepo.findById(sprintId)
                .orElseThrow(() -> new EntityNotFoundException("Sprint not found with id: " + sprintId));

        UserStory us = userStoryRepo.findById(userStoryId)
                .orElseThrow(() -> new EntityNotFoundException("UserStory not found with id: " + userStoryId));

        sprint.getUserStories().add(us);
        us.setSprintBacklog(sprint);

        SprintBacklog updated = sprintRepo.save(sprint);
        return sprintMapper.toDto(updated);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER')")
    public SprintBacklogDTO removeUserStoryFromSprint(Long sprintId, Long userStoryId) {
        SprintBacklog sprint = sprintRepo.findById(sprintId)
                .orElseThrow(() -> new EntityNotFoundException("Sprint not found with id: " + sprintId));

        UserStory us = userStoryRepo.findById(userStoryId)
                .orElseThrow(() -> new EntityNotFoundException("UserStory not found with id: " + userStoryId));

        sprint.getUserStories().remove(us);
        us.setSprintBacklog(null);

        SprintBacklog updated = sprintRepo.save(sprint);
        return sprintMapper.toDto(updated);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER', 'DEVELOPER')")
    public List<TaskDTO> getAllTasksInSprint(Long sprintId) {
        SprintBacklog sprint = sprintRepo.findById(sprintId)
                .orElseThrow(() -> new EntityNotFoundException("Sprint not found with id: " + sprintId));

        return sprint.getUserStories().stream()
                .flatMap(us -> us.getTasks().stream())
                .map(taskMapper::toDto)
                .collect(Collectors.toList());
    }
}
