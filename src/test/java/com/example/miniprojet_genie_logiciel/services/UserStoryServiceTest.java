package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.dto.*;
import com.example.miniprojet_genie_logiciel.entities.*;
import com.example.miniprojet_genie_logiciel.mapper.TaskMapper;
import com.example.miniprojet_genie_logiciel.mapper.UserStoryMapper;
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
    @Mock private UserStoryMapper userStoryMapper;
    @Mock private TaskMapper taskMapper;

    private UserStory userStory;
    private UserStoryDTO userStoryDTO;

    @BeforeEach
    void setup() {
        userStory = new UserStory();
        userStory.setId(1L);
        userStory.setTitle("Connexion");
        userStory.setTasks(new ArrayList<>());

        userStoryDTO = new UserStoryDTO();
        userStoryDTO.setId(1L);
        userStoryDTO.setTitle("Connexion");
        userStoryDTO.setTasks(new ArrayList<>());
    }

    @Test
    void shouldAddUserStory() {
        when(userStoryMapper.toEntity(userStoryDTO)).thenReturn(userStory);
        when(userStoryRepository.save(userStory)).thenReturn(userStory);
        when(userStoryMapper.toDto(userStory)).thenReturn(userStoryDTO);

        UserStoryDTO saved = userStoryService.addUserStory(userStoryDTO);

        assertNotNull(saved);
        assertEquals("Connexion", saved.getTitle());
        verify(userStoryRepository).save(userStory);
        verify(userStoryMapper).toDto(userStory);
    }

    @Test
    void shouldUpdateUserStory() {
        UserStoryDTO updatedDTO = new UserStoryDTO();
        updatedDTO.setTitle("Inscription");
        updatedDTO.setAction("faire");
        updatedDTO.setGoal("tester");
        updatedDTO.setRole("utilisateur");

        UserStory updatedEntity = new UserStory();
        updatedEntity.setTitle("Inscription");

        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(userStoryMapper.toEntity(updatedDTO)).thenReturn(updatedEntity);
        when(userStoryRepository.save(any())).thenReturn(userStory);
        when(userStoryMapper.toDto(userStory)).thenReturn(updatedDTO);

        UserStoryDTO result = userStoryService.updateUserStory(1L, updatedDTO);

        assertEquals("Inscription", result.getTitle());
        verify(userStoryRepository).save(any());
        verify(userStoryMapper).toDto(any());
    }

    @Test
    void shouldUpdatePriority() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        PriorityDTO priorityDTO = new PriorityDTO();
        priorityDTO.setName("Must Have");
        priorityDTO.setWeight(4);

        userStory.setPriority(Priority.MUST_HAVE);
        when(userStoryRepository.save(any())).thenReturn(userStory);

        UserStoryDTO resultDTO = new UserStoryDTO();
        resultDTO.setPriority(priorityDTO);
        when(userStoryMapper.toDto(userStory)).thenReturn(resultDTO);

        UserStoryDTO result = userStoryService.updateUserStoryPriority(1L, Priority.MUST_HAVE);

        assertEquals("Must Have", result.getPriority().getName());
        assertEquals(4, result.getPriority().getWeight());
        verify(userStoryMapper).toDto(userStory);
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

        UserStoryDTO resultDTO = new UserStoryDTO();
        EpicDTO epicDTO = new EpicDTO();
        epicDTO.setId(2L);
        resultDTO.setEpic(epicDTO);
        when(userStoryMapper.toDto(userStory)).thenReturn(resultDTO);

        UserStoryDTO result = userStoryService.linkUserStoryToEpic(1L, 2L);

        assertEquals(epicDTO.getId(), result.getEpic().getId());
        verify(userStoryMapper).toDto(userStory);
    }

    @Test
    void shouldSetAcceptanceCriteria() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(userStoryRepository.save(any())).thenReturn(userStory);

        UserStoryDTO resultDTO = new UserStoryDTO();
        resultDTO.setAcceptanceCriteria("Doit fonctionner sans erreur");
        when(userStoryMapper.toDto(userStory)).thenReturn(resultDTO);

        UserStoryDTO updated = userStoryService.setAcceptanceCriteria(1L, "Doit fonctionner sans erreur");

        assertEquals("Doit fonctionner sans erreur", updated.getAcceptanceCriteria());
        verify(userStoryMapper).toDto(userStory);
    }

    @Test
    void shouldUpdateStatus() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(userStoryRepository.save(any())).thenReturn(userStory);

        UserStoryDTO resultDTO = new UserStoryDTO();
        resultDTO.setStatus(StatusDTO.IN_PROGRESS);
        when(userStoryMapper.toDto(userStory)).thenReturn(resultDTO);

        UserStoryDTO updated = userStoryService.updateUserStoryStatus(1L, Status.IN_PROGRESS);

        assertEquals(StatusDTO.IN_PROGRESS, updated.getStatus());
        verify(userStoryMapper).toDto(userStory);
    }

    @Test
    void shouldAddTaskToUserStory() {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Faire test unitaire");
        taskDTO.setDescription("Implement unit tests");
        taskDTO.setStatus(StatusDTO.TO_DO);
        taskDTO.setEstimation(5);

        Task task = new Task();
        task.setTitle("Faire test unitaire");
        task.setDescription("Implement unit tests");
        task.setStatus(Status.TO_DO.toString());
        task.setEstimation(5);

        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(taskMapper.toEntity(taskDTO)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        TaskDTO added = userStoryService.addTaskToUserStory(1L, taskDTO);

        assertEquals("Faire test unitaire", added.getTitle());
        assertEquals("Implement unit tests", added.getDescription());
        assertEquals(StatusDTO.TO_DO, added.getStatus());
        assertEquals(5, added.getEstimation());
        verify(taskMapper).toDto(task);
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
        task.setStatus(Status.TO_DO.toString());

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(10L);
        taskDTO.setStatus(StatusDTO.DONE);

        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        TaskDTO updated = userStoryService.updateTaskStatus(10L, Status.DONE.toString());

        assertEquals(StatusDTO.DONE, updated.getStatus());
    }

    @Test
    void shouldGetTasksForUserStory() {
        Task task = new Task();
        task.setTitle("Implémenter DAO");
        userStory.getTasks().add(task);

        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTitle("Implémenter DAO");

        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(taskMapper.toDto(task)).thenReturn(taskDTO);

        List<TaskDTO> tasks = userStoryService.getTasksForUserStory(1L);

        assertEquals(1, tasks.size());
        assertEquals("Implémenter DAO", tasks.get(0).getTitle());
        verify(taskMapper).toDto(task);
    }
}

