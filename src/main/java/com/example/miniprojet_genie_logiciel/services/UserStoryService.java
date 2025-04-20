package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.dto.CreateUserStoryDTO;
import com.example.miniprojet_genie_logiciel.dto.UpdateUserStoryDTO;
import com.example.miniprojet_genie_logiciel.dto.UserStoryDTO;
import com.example.miniprojet_genie_logiciel.entities.*;
import com.example.miniprojet_genie_logiciel.mapper.UserStoryMapper;
import com.example.miniprojet_genie_logiciel.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserStoryService {

    private final UserStoryRepository userStoryRepository;
    private final EpicRepository epicRepository;
    private final ProductBacklogRepository productBacklogRepository;
    private final SprintBacklogRepository sprintBacklogRepository;
    private final UserStoryMapper userStoryMapper;

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER')")
    public UserStoryDTO createUserStory(CreateUserStoryDTO dto) {
        Epic epic = epicRepository.findById(dto.getEpicId())
                .orElseThrow(() -> new EntityNotFoundException("Epic not found with id: " + dto.getEpicId()));

        ProductBacklog backlog = productBacklogRepository.findById(dto.getProductBacklogId())
                .orElseThrow(() -> new EntityNotFoundException("ProductBacklog not found with id: " + dto.getProductBacklogId()));

        UserStory userStory = userStoryMapper.fromCreateDto(dto, epic, backlog);
        UserStory saved = userStoryRepository.save(userStory);
        return userStoryMapper.toDto(saved);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER')")
    public List<UserStoryDTO> getAllUserStories() {
        return userStoryRepository.findAll().stream()
                .map(userStoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER', 'DEVELOPER')")
    public UserStoryDTO getUserStoryById(Long id) {
        UserStory us = userStoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UserStory not found with id: " + id));
        return userStoryMapper.toDto(us);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER')")
    public UserStoryDTO updateUserStory(Long id, UpdateUserStoryDTO dto) {
        UserStory us = userStoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UserStory not found with id: " + id));

        SprintBacklog sprintBacklog = null;
        if (dto.getSprintBacklogId() != null) {
            sprintBacklog = sprintBacklogRepository.findById(dto.getSprintBacklogId())
                    .orElseThrow(() -> new EntityNotFoundException("SprintBacklog not found with id: " + dto.getSprintBacklogId()));
        }

        userStoryMapper.updateFromDto(dto, us, sprintBacklog);
        UserStory updated = userStoryRepository.save(us);
        return userStoryMapper.toDto(updated);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER')")
    public void deleteUserStory(Long id) {
        if (!userStoryRepository.existsById(id)) {
            throw new EntityNotFoundException("UserStory not found with id: " + id);
        }
        userStoryRepository.deleteById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER', 'DEVELOPER')")
    public List<UserStoryDTO> getUserStoriesByEpic(Long epicId) {
        Epic epic = epicRepository.findById(epicId)
                .orElseThrow(() -> new EntityNotFoundException("Epic not found with id: " + epicId));
        return epic.getUserStories().stream()
                .map(userStoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER', 'DEVELOPER')")
    public List<UserStoryDTO> getUserStoriesBySprintBacklog(Long sprintId) {
        SprintBacklog sprint = sprintBacklogRepository.findById(sprintId)
                .orElseThrow(() -> new EntityNotFoundException("SprintBacklog not found with id: " + sprintId));
        return sprint.getUserStories().stream()
                .map(userStoryMapper::toDto)
                .collect(Collectors.toList());
    }
}
