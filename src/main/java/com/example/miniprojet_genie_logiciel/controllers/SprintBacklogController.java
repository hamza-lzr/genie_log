package com.example.miniprojet_genie_logiciel.controllers;

import com.example.miniprojet_genie_logiciel.dto.SprintBacklogDTO;
import com.example.miniprojet_genie_logiciel.dto.UserStoryDTO;
import com.example.miniprojet_genie_logiciel.services.SprintBacklogService;
import com.example.miniprojet_genie_logiciel.mapper.SprintBacklogMapper;
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
    private final SprintBacklogMapper sprintBacklogMapper;


    @GetMapping
    public ResponseEntity<List<SprintBacklogDTO>> getAllSprints() {
        List<SprintBacklogDTO> sprints = sprintBacklogService.findAllSprints();
        return ResponseEntity.ok(sprints);
    }


    @GetMapping("/{id}")
    public ResponseEntity<SprintBacklogDTO> getSprintById(@PathVariable Long id) {
        return ResponseEntity.ok(sprintBacklogService.findSprintById(id));
    }

    // Création d'un SprintBacklog à partir d'un objet complet
    @PostMapping
    public ResponseEntity<SprintBacklogDTO> createSprintBacklog(@RequestBody SprintBacklogDTO sprintBacklogDTO) {
        SprintBacklogDTO created = sprintBacklogService.createSprint(sprintBacklogDTO.getName());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }

    // Création d'un nouveau Sprint en spécifiant son nom
    @PostMapping("/create")
    public ResponseEntity<SprintBacklogDTO> createSprint(@RequestParam String name) {
        SprintBacklogDTO created = sprintBacklogService.createSprint(name);
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
    public ResponseEntity<SprintBacklogDTO> addUserStoryToSprint(@PathVariable Long sprintId,
                                                              @PathVariable Long usId) {
            SprintBacklogDTO updated = sprintBacklogService.addUserStoryToSprint(sprintId, usId);
            return ResponseEntity.ok(updated);
    }

    // Suppression d'une User Story d'un Sprint
    @DeleteMapping("/{sprintId}/userstories/{usId}")
    public ResponseEntity<Void> removeUserStoryFromSprint(@PathVariable Long sprintId,
                                                                   @PathVariable Long usId) {
           sprintBacklogService.removeUserStoryFromSprint(sprintId, usId);
           return ResponseEntity.noContent().build();
    }

}

