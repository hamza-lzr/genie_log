package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.entities.*;
import com.example.miniprojet_genie_logiciel.repository.EpicRepository;
import com.example.miniprojet_genie_logiciel.repository.ProductBacklogRepository;
import com.example.miniprojet_genie_logiciel.repository.TaskRepository;
import com.example.miniprojet_genie_logiciel.repository.UserStoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserStoryServiceTest {

    @InjectMocks
    private UserStoryService userStoryService;

    @Mock private UserStoryRepository userStoryRepository;
    @Mock private EpicRepository epicRepository;
    @Mock private ProductBacklogRepository productBacklogRepository;
    @Mock private TaskRepository taskRepository;

    private UserStory userStory;

    @BeforeEach
    void setup() {
        userStory = new UserStory();
        userStory.setId(1L);
        userStory.setTitle("Connexion");
        userStory.setTasks(new ArrayList<>());
    }

    @Test
    void shouldAddUserStory() {
        when(userStoryRepository.save(userStory)).thenReturn(userStory);

        UserStory saved = userStoryService.addUserStory(userStory);

        assertNotNull(saved);
        assertEquals("Connexion", saved.getTitle());
        verify(userStoryRepository, times(1)).save(userStory);
    }

    @Test
    void shouldUpdateUserStory() {
        UserStory updated = new UserStory();
        updated.setTitle("Inscription");
        updated.setAction("faire");
        updated.setGoal("tester");
        updated.setRole("utilisateur");

        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(userStoryRepository.save(any())).thenReturn(userStory);

        UserStory result = userStoryService.updateUserStory(1L, updated);

        assertEquals("Inscription", result.getTitle());
        verify(userStoryRepository).save(userStory);
    }

    @Test
    void shouldUpdatePriority() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        userStory.setPriority(Priority.MUST_HAVE);
        when(userStoryRepository.save(any())).thenReturn(userStory); // <- AJOUTÉ


        UserStory result = userStoryService.updateUserStoryPriority(1L, Priority.MUST_HAVE);

        assertEquals(Priority.MUST_HAVE, result.getPriority());
    }

    @Test
    void shouldDeleteUserStory() {
        when(userStoryRepository.existsById(1L)).thenReturn(true);

        userStoryService.deleteUserStory(1L);

        verify(userStoryRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentStory() {
        when(userStoryRepository.existsById(99L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () ->
                userStoryService.deleteUserStory(99L));
    }

    @Test
    void shouldLinkUserStoryToEpic() {
        Epic epic = new Epic();
        epic.setId(2L);
        epic.setUserStories(new ArrayList<>());

        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(epicRepository.findById(2L)).thenReturn(Optional.of(epic));
        when(userStoryRepository.save(userStory)).thenReturn(userStory);

        UserStory result = userStoryService.linkUserStoryToEpic(1L, 2L);

        assertEquals(epic, result.getEpic());
        assertTrue(epic.getUserStories().contains(userStory));
    }

    @Test
    void shouldSetAcceptanceCriteria() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(userStoryRepository.save(any())).thenReturn(userStory); // <- AJOUTÉ


        UserStory updated = userStoryService.setAcceptanceCriteria(1L, "Doit fonctionner sans erreur");

        assertEquals("Doit fonctionner sans erreur", updated.getAcceptanceCriteria());
    }

    @Test
    void shouldUpdateStatus() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(userStoryRepository.save(any())).thenReturn(userStory);


        UserStory updated = userStoryService.updateUserStoryStatus(1L, Status.IN_PROGRESS);

        assertEquals(Status.IN_PROGRESS, updated.getStatus());
    }

    @Test
    void shouldAddTaskToUserStory() {
        Task task = new Task();
        task.setTitle("Faire test unitaire");

        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));

        Task added = userStoryService.addTaskToUserStory(1L, task);

        assertEquals("Faire test unitaire", added.getTitle());
        assertTrue(userStory.getTasks().contains(task));
    }

    @Test
    void shouldDeleteTaskFromUserStory() {
        Task task = new Task();
        task.setId(10L);
        task.setTitle("Tâche à supprimer");

        userStory.getTasks().add(task);

        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

        userStoryService.deleteTaskFromUserStory(1L, 10L);

        assertFalse(userStory.getTasks().contains(task));
        verify(taskRepository).delete(task);
    }

    @Test
    void shouldUpdateTaskStatus() {
        Task task = new Task();
        task.setId(10L);
        task.setStatus("To Do");

        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        Task updated = userStoryService.updateTaskStatus(10L, "Done");

        assertEquals("Done", updated.getStatus());
    }

    @Test
    void shouldGetTasksForUserStory() {
        Task task = new Task();
        task.setTitle("Implémenter DAO");
        userStory.getTasks().add(task);

        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));

        List<Task> tasks = userStoryService.getTasksForUserStory(1L);

        assertEquals(1, tasks.size());
        assertEquals("Implémenter DAO", tasks.get(0).getTitle());
    }
}

