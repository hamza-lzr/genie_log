package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.dto.SprintBacklogDTO;
import com.example.miniprojet_genie_logiciel.dto.UserStoryDTO;
import com.example.miniprojet_genie_logiciel.entities.SprintBacklog;
import com.example.miniprojet_genie_logiciel.entities.UserStory;
import com.example.miniprojet_genie_logiciel.mapper.SprintBacklogMapper;
import com.example.miniprojet_genie_logiciel.mapper.UserStoryMapper;
import com.example.miniprojet_genie_logiciel.repository.SprintBacklogRepository;
import com.example.miniprojet_genie_logiciel.repository.UserStoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SprintBacklogServiceTest {

    @InjectMocks
    private SprintBacklogService sprintBacklogService;

    @Mock
    private SprintBacklogRepository sprintBacklogRepository;

    @Mock
    private UserStoryRepository userStoryRepository;

    @Mock
    private SprintBacklogMapper sprintBacklogMapper;

    @Mock
    private UserStoryMapper userStoryMapper;

    @Test
    void shouldCreateSprint() {
        SprintBacklog sprint = new SprintBacklog();
        sprint.setName("Sprint 1");
        sprint.setUserStories(new ArrayList<>());

        SprintBacklogDTO sprintDTO = new SprintBacklogDTO();
        sprintDTO.setName("Sprint 1");
        sprintDTO.setUserStories(new ArrayList<>());

        when(sprintBacklogRepository.save(any(SprintBacklog.class))).thenReturn(sprint);
        when(sprintBacklogMapper.toDto(any(SprintBacklog.class))).thenReturn(sprintDTO);

        SprintBacklogDTO created = sprintBacklogService.createSprint("Sprint 1");

        assertNotNull(created);
        assertEquals("Sprint 1", created.getName());
        verify(sprintBacklogRepository).save(any(SprintBacklog.class));
        verify(sprintBacklogMapper).toDto(any(SprintBacklog.class));
    }

    @Test
    void shouldAddUserStoryToSprint() {
        SprintBacklog sprint = new SprintBacklog();
        sprint.setId(1L);
        sprint.setName("Sprint A");
        sprint.setUserStories(new ArrayList<>());

        UserStory us = new UserStory();
        us.setId(2L);

        SprintBacklogDTO sprintDTO = new SprintBacklogDTO();
        sprintDTO.setId(1L);
        sprintDTO.setName("Sprint A");

        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprint));
        when(userStoryRepository.findById(2L)).thenReturn(Optional.of(us));
        when(sprintBacklogRepository.save(any(SprintBacklog.class))).thenReturn(sprint);
        when(sprintBacklogMapper.toDto(any(SprintBacklog.class))).thenReturn(sprintDTO);

        SprintBacklogDTO result = sprintBacklogService.addUserStoryToSprint(1L, 2L);

        assertNotNull(result);
        assertEquals("Sprint A", result.getName());
        verify(sprintBacklogMapper).toDto(any(SprintBacklog.class));
    }

    @Test
    void shouldThrowIfUserStoryAlreadyAssignedToSprint() {
        SprintBacklog sprint = new SprintBacklog();
        sprint.setId(1L);

        UserStory us = new UserStory();
        us.setId(2L);
        us.setSprintBacklog(new SprintBacklog()); // already assigned

        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprint));
        when(userStoryRepository.findById(2L)).thenReturn(Optional.of(us));

        assertThrows(IllegalStateException.class, () ->
                sprintBacklogService.addUserStoryToSprint(1L, 2L));
    }

    @Test
    void shouldRemoveUserStoryFromSprint() {
        SprintBacklog sprint = new SprintBacklog();
        sprint.setId(1L);
        List<UserStory> stories = new ArrayList<>();
        UserStory us = new UserStory();
        us.setId(2L);
        us.setSprintBacklog(sprint);
        stories.add(us);
        sprint.setUserStories(stories);

        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprint));
        when(userStoryRepository.findById(2L)).thenReturn(Optional.of(us));

        sprintBacklogService.removeUserStoryFromSprint(1L, 2L);

        assertFalse(sprint.getUserStories().contains(us));
        assertNull(us.getSprintBacklog());
    }

    @Test
    void shouldThrowWhenRemovingUserStoryNotInSprint() {
        SprintBacklog sprint = new SprintBacklog();
        sprint.setId(1L);
        sprint.setUserStories(new ArrayList<>());

        UserStory us = new UserStory();
        us.setId(2L);

        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprint));
        when(userStoryRepository.findById(2L)).thenReturn(Optional.of(us));

        assertThrows(IllegalArgumentException.class, () ->
                sprintBacklogService.removeUserStoryFromSprint(1L, 2L));
    }

    @Test
    void shouldFindSprintById() {
        SprintBacklog sprint = new SprintBacklog();
        sprint.setId(1L);

        SprintBacklogDTO sprintDTO = new SprintBacklogDTO();
        sprintDTO.setId(1L);

        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprint));
        when(sprintBacklogMapper.toDto(sprint)).thenReturn(sprintDTO);

        SprintBacklogDTO found = sprintBacklogService.findSprintById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        verify(sprintBacklogMapper).toDto(sprint);
    }

    @Test
    void shouldThrowWhenSprintNotFound() {
        when(sprintBacklogRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                sprintBacklogService.findSprintById(999L));
    }

    @Test
    void shouldDeleteSprint() {
        when(sprintBacklogRepository.existsById(1L)).thenReturn(true);

        sprintBacklogService.deleteSprintById(1L);

        verify(sprintBacklogRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentSprint() {
        when(sprintBacklogRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () ->
                sprintBacklogService.deleteSprintById(99L));
    }
}