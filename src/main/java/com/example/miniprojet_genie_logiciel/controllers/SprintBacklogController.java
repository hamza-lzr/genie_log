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


    @GetMapping
    public ResponseEntity<List<SprintBacklog>> getAllSprints() {
        List<SprintBacklog> sprints = sprintBacklogService.findAllSprints();
        return ResponseEntity.ok(sprints);
    }


    @GetMapping("/{id}")
    public ResponseEntity<SprintBacklog> getSprintById(@PathVariable Long id) {
        return ResponseEntity.ok(sprintBacklogService.findSprintById(id));

    }

    // Création d'un SprintBacklog à partir d'un objet complet
    @PostMapping
    public ResponseEntity<SprintBacklog> createSprintBacklog(@RequestBody SprintBacklog sprintBacklog) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(sprintBacklog);
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
            sprintBacklogService.deleteSprintById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Ajout d'une User Story à un Sprint
    @PostMapping("/{sprintId}/userstories/{usId}")
    public ResponseEntity<String> addUserStoryToSprint(@PathVariable Long sprintId,
                                                              @PathVariable Long usId) {
            return ResponseEntity.ok(sprintBacklogService.addUserStoryToSprint(sprintId, usId));

    }

    // Suppression d'une User Story d'un Sprint
    @DeleteMapping("/{sprintId}/userstories/{usId}")
    public ResponseEntity<String> removeUserStoryFromSprint(@PathVariable Long sprintId,
                                                                   @PathVariable Long usId) {
           sprintBacklogService.removeUserStoryFromSprint(sprintId, usId);
           return ResponseEntity.noContent().build();

    }

}
