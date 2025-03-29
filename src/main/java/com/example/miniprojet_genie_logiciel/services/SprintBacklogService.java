package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.entities.SprintBacklog;
import com.example.miniprojet_genie_logiciel.entities.UserStory;
import com.example.miniprojet_genie_logiciel.entities.Task;
import com.example.miniprojet_genie_logiciel.repository.SprintBacklogRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SprintBacklogService {

    private final SprintBacklogRepository sprintbacklogrepository;

    // Méthodes CRUD de base
    public SprintBacklog saveSprintBacklog(SprintBacklog sprintbacklog) {
        return sprintbacklogrepository.save(sprintbacklog);
    }

    public List<SprintBacklog> findAll() {
        return sprintbacklogrepository.findAll();
    }

    public Optional<SprintBacklog> findById(Long id) {
        return sprintbacklogrepository.findById(id);
    }

    public void deleteById(Long id) {
        sprintbacklogrepository.deleteById(id);
    }

    // Création d'un nouveau Sprint (Sprint 1, Sprint 2, etc.)
    public SprintBacklog createSprint(String name) {
        SprintBacklog sprint = new SprintBacklog();
        sprint.setName(name);
        sprint.setUserStories(new ArrayList<>());
        sprint.setTasks(new ArrayList<>());
        return sprintbacklogrepository.save(sprint);
    }

    // Sélection et ajout d'une User Story depuis le Product Backlog dans le Sprint Backlog
    public SprintBacklog addUserStoryToSprint(Long sprintId, UserStory userStory) {
        SprintBacklog sprint = sprintbacklogrepository.findById(sprintId)
                .orElseThrow(() -> new EntityNotFoundException("Sprint not found with id: " + sprintId));
        sprint.getUserStories().add(userStory);
        return sprintbacklogrepository.save(sprint);
    }

    // Suppression d'une User Story du Sprint Backlog
    public SprintBacklog removeUserStoryFromSprint(Long sprintId, UserStory userStory) {
        SprintBacklog sprint = sprintbacklogrepository.findById(sprintId)
                .orElseThrow(() -> new EntityNotFoundException("Sprint not found with id: " + sprintId));
        sprint.getUserStories().remove(userStory);
        return sprintbacklogrepository.save(sprint);
    }

    // Ajout d'une Task au Sprint Backlog (liée à une User Story)
    public SprintBacklog addTaskToSprint(Long sprintId, Task task) {
        SprintBacklog sprint = sprintbacklogrepository.findById(sprintId)
                .orElseThrow(() -> new EntityNotFoundException("Sprint not found with id: " + sprintId));
        sprint.getTasks().add(task);
        return sprintbacklogrepository.save(sprint);
    }

    // Mise à jour de l'état d'une Task dans le Sprint Backlog
    public SprintBacklog updateTaskStatus(Long sprintId, Long taskId, String newStatus) {
        SprintBacklog sprint = sprintbacklogrepository.findById(sprintId)
                .orElseThrow(() -> new EntityNotFoundException("Sprint not found with id: " + sprintId));
        sprint.getTasks().forEach(task -> {
            if (task.getId().equals(taskId)) {
                task.setStatus(newStatus);
            }
        });
        return sprintbacklogrepository.save(sprint);
    }

   /* // Suivi et mise à jour de l'état d'une User Story dans le Sprint Backlog
    public SprintBacklog updateUserStoryStatus(Long sprintId, Long userStoryId, String newStatus) {
        SprintBacklog sprint = sprintbacklogrepository.findById(sprintId)
                .orElseThrow(() -> new EntityNotFoundException("Sprint not found with id: " + sprintId));
        sprint.getUserStories().forEach(us -> {
            if (us.getId().equals(userStoryId)) {
                us.setStatus(newStatus);
            }
        });
        return sprintbacklogrepository.save(sprint);
    }

    */

}

