package com.example.miniprojet_genie_logiciel.controllers;

import com.example.miniprojet_genie_logiciel.entities.SprintBacklog;
import com.example.miniprojet_genie_logiciel.entities.UserStory;
import com.example.miniprojet_genie_logiciel.entities.Task;
import com.example.miniprojet_genie_logiciel.services.SprintBacklogService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sprint-backlogs")
@RequiredArgsConstructor
public class SprintBacklogController {

    private final SprintBacklogService sprintBacklogService;

    // Récupérer tous les sprints
    @GetMapping
    public ResponseEntity<List<SprintBacklog>> getAllSprints() {
        List<SprintBacklog> sprints = sprintBacklogService.findAll();
        return ResponseEntity.ok(sprints);
    }

    // Récupérer un sprint par son id
    @GetMapping("/{id}")
    public ResponseEntity<SprintBacklog> getSprintById(@PathVariable Long id) {
        return sprintBacklogService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Création d'un SprintBacklog à partir d'un objet complet
    @PostMapping
    public ResponseEntity<SprintBacklog> createSprintBacklog(@RequestBody SprintBacklog sprintBacklog) {
        SprintBacklog created = sprintBacklogService.saveSprintBacklog(sprintBacklog);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Création d'un nouveau Sprint en spécifiant son nom
    @PostMapping("/create")
    public ResponseEntity<SprintBacklog> createSprint(@RequestParam String name) {
        SprintBacklog created = sprintBacklogService.createSprint(name);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Suppression d'un sprint par son id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSprint(@PathVariable Long id) {
        try {
            sprintBacklogService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Ajout d'une User Story à un Sprint
    @PostMapping("/{sprintId}/userstories")
    public ResponseEntity<SprintBacklog> addUserStoryToSprint(@PathVariable Long sprintId,
                                                              @RequestBody UserStory userStory) {
        try {
            SprintBacklog updated = sprintBacklogService.addUserStoryToSprint(sprintId, userStory);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Suppression d'une User Story d'un Sprint
    @DeleteMapping("/{sprintId}/userstories")
    public ResponseEntity<SprintBacklog> removeUserStoryFromSprint(@PathVariable Long sprintId,
                                                                   @RequestBody UserStory userStory) {
        try {
            SprintBacklog updated = sprintBacklogService.removeUserStoryFromSprint(sprintId, userStory);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Ajout d'une Task à un Sprint
    @PostMapping("/{sprintId}/tasks")
    public ResponseEntity<SprintBacklog> addTaskToSprint(@PathVariable Long sprintId,
                                                         @RequestBody Task task) {
        try {
            SprintBacklog updated = sprintBacklogService.addTaskToSprint(sprintId, task);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Mise à jour de l'état d'une Task dans un Sprint
    @PutMapping("/{sprintId}/tasks/{taskId}/status")
    public ResponseEntity<SprintBacklog> updateTaskStatus(@PathVariable Long sprintId,
                                                          @PathVariable Long taskId,
                                                          @RequestParam String status) {
        try {
            SprintBacklog updated = sprintBacklogService.updateTaskStatus(sprintId, taskId, status);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Mise à jour de l'état d'une User Story dans un Sprint
    @PutMapping("/{sprintId}/userstories/{userStoryId}/status")
    public ResponseEntity<SprintBacklog> updateUserStoryStatus(@PathVariable Long sprintId,
                                                               @PathVariable Long userStoryId,
                                                               @RequestParam String status) {
        try {
            SprintBacklog updated = sprintBacklogService.updateUserStoryStatus(sprintId, userStoryId, status);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
