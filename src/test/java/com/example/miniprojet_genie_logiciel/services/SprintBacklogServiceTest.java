package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.entities.SprintBacklog;
import com.example.miniprojet_genie_logiciel.entities.UserStory;
import com.example.miniprojet_genie_logiciel.entities.Task;
import com.example.miniprojet_genie_logiciel.repository.SprintBacklogRepository;
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

    @Mock
    private SprintBacklogRepository sprintBacklogRepository;

    @InjectMocks
    private SprintBacklogService sprintBacklogService;

    private SprintBacklog sprintBacklog;
    private UserStory userStory;
    private Task task;

    @BeforeEach
    void setUp() {
        sprintBacklog = new SprintBacklog();
        sprintBacklog.setId(1L);
        sprintBacklog.setName("Sprint 1");
        sprintBacklog.setUserStories(new ArrayList<>());
        sprintBacklog.setTasks(new ArrayList<>());

        userStory = new UserStory();
        userStory.setId(1L);
        userStory.setTitle("Test User Story");

        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setStatus("TO_DO");
    }

    @Test
    void testSaveSprintBacklog() {
        when(sprintBacklogRepository.save(any(SprintBacklog.class))).thenReturn(sprintBacklog);

        SprintBacklog saved = sprintBacklogService.saveSprintBacklog(sprintBacklog);

        assertNotNull(saved);
        assertEquals("Sprint 1", saved.getName());
        verify(sprintBacklogRepository, times(1)).save(any(SprintBacklog.class));
    }

    @Test
    void testFindAll() {
        when(sprintBacklogRepository.findAll()).thenReturn(List.of(sprintBacklog));

        List<SprintBacklog> sprints = sprintBacklogService.findAll();

        assertFalse(sprints.isEmpty());
        assertEquals(1, sprints.size());
        assertEquals("Sprint 1", sprints.get(0).getName());
    }

    @Test
    void testFindById() {
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprintBacklog));

        Optional<SprintBacklog> found = sprintBacklogService.findById(1L);

        assertTrue(found.isPresent());
        assertEquals("Sprint 1", found.get().getName());
    }

    @Test
    void testDeleteById() {
        doNothing().when(sprintBacklogRepository).deleteById(1L);

        assertDoesNotThrow(() -> sprintBacklogService.deleteById(1L));
        verify(sprintBacklogRepository, times(1)).deleteById(1L);
    }

    @Test
    void testCreateSprint() {
        when(sprintBacklogRepository.save(any(SprintBacklog.class))).thenReturn(sprintBacklog);

        SprintBacklog created = sprintBacklogService.createSprint("Sprint 1");

        assertNotNull(created);
        assertEquals("Sprint 1", created.getName());
        assertNotNull(created.getUserStories());
        assertNotNull(created.getTasks());
        assertTrue(created.getUserStories().isEmpty());
        assertTrue(created.getTasks().isEmpty());
    }

    @Test
    void testAddUserStoryToSprint() {
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprintBacklog));
        when(sprintBacklogRepository.save(any(SprintBacklog.class))).thenReturn(sprintBacklog);

        SprintBacklog updated = sprintBacklogService.addUserStoryToSprint(1L, userStory);

        assertNotNull(updated);
        assertTrue(updated.getUserStories().contains(userStory));
    }

    @Test
    void testAddUserStoryToSprint_SprintNotFound() {
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, 
            () -> sprintBacklogService.addUserStoryToSprint(1L, userStory));
    }

    @Test
    void testRemoveUserStoryFromSprint() {
        sprintBacklog.getUserStories().add(userStory);
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprintBacklog));
        when(sprintBacklogRepository.save(any(SprintBacklog.class))).thenReturn(sprintBacklog);

        SprintBacklog updated = sprintBacklogService.removeUserStoryFromSprint(1L, userStory);

        assertNotNull(updated);
        assertFalse(updated.getUserStories().contains(userStory));
    }

    @Test
    void testAddTaskToSprint() {
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprintBacklog));
        when(sprintBacklogRepository.save(any(SprintBacklog.class))).thenReturn(sprintBacklog);

        SprintBacklog updated = sprintBacklogService.addTaskToSprint(1L, task);

        assertNotNull(updated);
        assertTrue(updated.getTasks().contains(task));
    }

    @Test
    void testUpdateTaskStatus() {
        sprintBacklog.getTasks().add(task);
        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprintBacklog));
        when(sprintBacklogRepository.save(any(SprintBacklog.class))).thenReturn(sprintBacklog);

        SprintBacklog updated = sprintBacklogService.updateTaskStatus(1L, 1L, "IN_PROGRESS");

        assertNotNull(updated);
        assertEquals("IN_PROGRESS", updated.getTasks().get(0).getStatus());
    }
}