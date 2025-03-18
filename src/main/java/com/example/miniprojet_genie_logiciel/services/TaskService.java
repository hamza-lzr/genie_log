package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.entities.Task;
import com.example.miniprojet_genie_logiciel.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    // Création d'une Task
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    // Récupération de toutes les Tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Récupération d'une Task par son id
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    // Mise à jour d'une Task (incluant le statut)
    public Task updateTask(Task task) {
        if (!taskRepository.existsById(task.getId())) {
            throw new EntityNotFoundException("Task not found with id: " + task.getId());
        }
        return taskRepository.save(task);
    }

    // Suppression d'une Task par son id
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new EntityNotFoundException("Task not found with id: " + id);
        }
        taskRepository.deleteById(id);
    }

    // Mise à jour du status d'une Task (ex: To Do, In Progress, Done)
    public Task updateTaskStatus(Long taskId, String status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + taskId));
        task.setStatus(status);
        return taskRepository.save(task);
    }
}
