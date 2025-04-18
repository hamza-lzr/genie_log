package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.dto.StatusDTO;

import com.example.miniprojet_genie_logiciel.dto.TaskDTO;
import com.example.miniprojet_genie_logiciel.entities.Task;
import com.example.miniprojet_genie_logiciel.mapper.TaskMapper;
import com.example.miniprojet_genie_logiciel.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private TaskDTO taskDTO;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(StatusDTO.TO_DO.toString());

        taskDTO = new TaskDTO();
        taskDTO.setId(1L);
        taskDTO.setTitle("Test Task");
        taskDTO.setDescription("Test Description");
        taskDTO.setStatus(StatusDTO.TO_DO);
    }

    @Test
    void testCreateTask() {
        when(taskMapper.toEntity(any(TaskDTO.class))).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toDto(any(Task.class))).thenReturn(taskDTO);

        TaskDTO created = taskService.createTask(taskDTO);

        assertNotNull(created);
        assertEquals("Test Task", created.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(taskMapper, times(1)).toEntity(any(TaskDTO.class));
        verify(taskMapper, times(1)).toDto(any(Task.class));
    }

    @Test
    void testGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(task));
        when(taskMapper.toDto(any(Task.class))).thenReturn(taskDTO);

        List<TaskDTO> tasks = taskService.getAllTasks();

        assertFalse(tasks.isEmpty());
        assertEquals(1, tasks.size());
        assertEquals("Test Task", tasks.get(0).getTitle());
        verify(taskMapper, times(1)).toDto(any(Task.class));
    }

    @Test
    void testGetTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(any(Task.class))).thenReturn(taskDTO);

        Optional<TaskDTO> found = taskService.getTaskById(1L);

        assertTrue(found.isPresent());
        assertEquals("Test Task", found.get().getTitle());
        verify(taskMapper, times(1)).toDto(any(Task.class));
    }

    @Test
    void testUpdateTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        when(taskMapper.toEntity(any(TaskDTO.class))).thenReturn(task);
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toDto(any(Task.class))).thenReturn(taskDTO);

        taskDTO.setTitle("Updated Task");
        TaskDTO updated = taskService.updateTask(taskDTO);

        assertEquals("Updated Task", updated.getTitle());
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(taskMapper, times(1)).toEntity(any(TaskDTO.class));
        verify(taskMapper, times(1)).toDto(any(Task.class));
    }

    @Test
    void testUpdateTask_NotFound() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        taskDTO.setTitle("Updated Task");
        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> taskService.updateTask(taskDTO));
        assertTrue(exception.getMessage().contains("Task not found"));
    }

    @Test
    void testDeleteTask() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        assertDoesNotThrow(() -> taskService.deleteTask(1L));
        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTask_NotFound() {
        when(taskRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> taskService.deleteTask(1L));
        assertTrue(exception.getMessage().contains("Task not found"));
    }

    @Test
    void testUpdateTaskStatus() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO updatedTask = taskService.updateTaskStatus(1L, StatusDTO.IN_PROGRESS.toString());

        assertEquals(StatusDTO.IN_PROGRESS, updatedTask.getStatus());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testUpdateTaskStatus_NotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> taskService.updateTaskStatus(1L, StatusDTO.IN_PROGRESS.toString()));
        assertTrue(exception.getMessage().contains("Task not found"));
    }
}