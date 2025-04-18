package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.dto.TaskDTO;
import com.example.miniprojet_genie_logiciel.dto.UserStoryDTO;
import com.example.miniprojet_genie_logiciel.entities.*;
import com.example.miniprojet_genie_logiciel.mapper.TaskMapper;
import com.example.miniprojet_genie_logiciel.mapper.UserStoryMapper;
import com.example.miniprojet_genie_logiciel.repository.EpicRepository;
import com.example.miniprojet_genie_logiciel.repository.ProductBacklogRepository;
import com.example.miniprojet_genie_logiciel.repository.TaskRepository;
import com.example.miniprojet_genie_logiciel.repository.UserStoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserStoryService {

    private final UserStoryRepository userStoryRepository;
    private final EpicRepository epicRepository;
    private final ProductBacklogRepository productBacklogRepository;
    private final TaskRepository taskRepository;
    private final UserStoryMapper userStoryMapper;
    private final TaskMapper taskMapper;


    // ==== CRUD Operations ====
    public UserStoryDTO addUserStory(UserStoryDTO userStoryDTO) {
        UserStory userStory = userStoryMapper.toEntity(userStoryDTO);
        UserStory savedUserStory = userStoryRepository.save(userStory);
        return userStoryMapper.toDto(savedUserStory);
    }

    public UserStoryDTO updateUserStory(Long id, UserStoryDTO updatedStoryDTO) {
        UserStory existing = getUserStoryOrThrow(id);
        UserStory updatedStory = userStoryMapper.toEntity(updatedStoryDTO);
        existing.setTitle(updatedStory.getTitle());
        existing.setAction(updatedStory.getAction());
        existing.setRole(updatedStory.getRole());
        existing.setGoal(updatedStory.getGoal());
        UserStory savedUserStory = userStoryRepository.save(existing);
        return userStoryMapper.toDto(savedUserStory);
    }


    public UserStoryDTO updateUserStoryPriority(Long id, Priority priority) {
        UserStory existing = getUserStoryOrThrow(id);
        existing.setPriority(priority);
        UserStory savedUserStory = userStoryRepository.save(existing);
        return userStoryMapper.toDto(savedUserStory);
    }


    public void deleteUserStory(Long id) {
        if (!userStoryRepository.existsById(id)) {
            throw new EntityNotFoundException("User Story non trouvée avec l'ID : " + id);
        }
        userStoryRepository.deleteById(id);
    }

    // ==== Business Logic ====
    public UserStoryDTO linkUserStoryToEpic(Long userStoryId, Long epicId) {
        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new EntityNotFoundException("UserStory not found with id: " + userStoryId));
        Epic epic = epicRepository.findById(epicId)
                .orElseThrow(() -> new EntityNotFoundException("Epic not found with id: " + epicId));

        userStory.setEpic(epic);

        if (!epic.getUserStories().contains(userStory)) {
            epic.getUserStories().add(userStory);
        }

        UserStory savedUserStory = userStoryRepository.save(userStory);
        return userStoryMapper.toDto(savedUserStory);
    }

    public UserStoryDTO unlinkUserStoryFromEpic(Long userStoryId, Long EpicId) {
        Optional<UserStory> userStory = userStoryRepository.findById(userStoryId);
        Optional<Epic> epic = epicRepository.findById(EpicId);
        if (userStory.isPresent() && epic.isPresent()) {
            UserStory us = userStory.get();
            Epic ep = epic.get();
            us.setEpic(null);
            ep.getUserStories().remove(us);
            UserStory savedUserStory = userStoryRepository.save(us);
            return userStoryMapper.toDto(savedUserStory);
        }
        return null;
    }

    public UserStoryDTO setAcceptanceCriteria(Long userStoryId, String criteria) {
        UserStory userStory = getUserStoryOrThrow(userStoryId);
        userStory.setAcceptanceCriteria(criteria);
        UserStory savedUserStory = userStoryRepository.save(userStory);
        return userStoryMapper.toDto(savedUserStory);
    }

    public UserStoryDTO updateUserStoryStatus(Long userStoryId, Status status) {
        UserStory userStory = getUserStoryOrThrow(userStoryId);
        userStory.setStatus(status);
        UserStory savedUserStory = userStoryRepository.save(userStory);
        return userStoryMapper.toDto(savedUserStory);
    }



    public List<UserStoryDTO> getPrioritizedUserStories(Long backlogId) {
        return productBacklogRepository.findById(backlogId)
                .orElseThrow(() -> new EntityNotFoundException("Product Backlog non trouvé"))
                .getUserStories().stream()
                .sorted((us1, us2) -> Integer.compare(
                        us1.getPriority().getWeight(),
                        us2.getPriority().getWeight()
                ))
                .map(userStoryMapper::toDto)
                .toList();
    }

    // ==== Query Methods ====
    public UserStoryDTO getUserStoryById(Long id) {
        UserStory userStory = userStoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User Story non trouvée avec l'ID : " + id));
        return userStoryMapper.toDto(userStory);
    }

    public List<UserStoryDTO> getAllUserStories() {
        return userStoryRepository.findAll().stream()
                .map(userStoryMapper::toDto)
                .toList();
    }

    private UserStory getUserStoryOrThrow(Long id) {
        return userStoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User Story non trouvée avec l'ID : " + id));
    }

    public TaskDTO addTaskToUserStory(Long userStoryId, TaskDTO taskDTO) {
        UserStory userStory = getUserStoryOrThrow(userStoryId);
        Task task = taskMapper.toEntity(taskDTO);
        task.setUserStory(userStory);
        userStory.getTasks().add(task);
        userStoryRepository.save(userStory); // persist cascade
        return taskMapper.toDto(task);
    }

    public void deleteTaskFromUserStory(Long userStoryId, Long taskId) {
        UserStory userStory = getUserStoryOrThrow(userStoryId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task non trouvée avec l'ID : " + taskId));

        if (!userStory.getTasks().contains(task)) {
            throw new IllegalArgumentException("Cette tâche n'appartient pas à la User Story spécifiée.");
        }

        userStory.getTasks().remove(task);
        taskRepository.delete(task); // suppression explicite
    }

    public TaskDTO updateTaskStatus(Long taskId, String newStatus) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task non trouvée avec l'ID : " + taskId));
        task.setStatus(newStatus);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);
    }

    public List<TaskDTO> getTasksForUserStory(Long userStoryId) {
        UserStory userStory = getUserStoryOrThrow(userStoryId);
        return userStory.getTasks().stream()
                .map(taskMapper::toDto)
                .toList();
    }

}


