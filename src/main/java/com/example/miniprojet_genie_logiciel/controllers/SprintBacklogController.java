package com.example.miniprojet_genie_logiciel.controllers;

import com.example.miniprojet_genie_logiciel.dto.CreateSprintBacklogDTO;
import com.example.miniprojet_genie_logiciel.dto.SprintBacklogDTO;
import com.example.miniprojet_genie_logiciel.services.SprintBacklogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sprints")
@RequiredArgsConstructor
public class SprintBacklogController {

    private final SprintBacklogService sprintBacklogService;

    // ✅ Créer un nouveau Sprint
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER')")
    public ResponseEntity<SprintBacklogDTO> createSprint(@RequestBody CreateSprintBacklogDTO dto) {
        SprintBacklogDTO createdSprint = sprintBacklogService.createSprint(dto);
        return ResponseEntity.ok(createdSprint);
    }

    // ✅ Lister tous les sprints
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER', 'DEVELOPER')")
    public ResponseEntity<List<SprintBacklogDTO>> getAllSprints() {
        return ResponseEntity.ok(sprintBacklogService.getAllSprints());
    }

    // ✅ Consulter un sprint par ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER', 'DEVELOPER')")
    public ResponseEntity<SprintBacklogDTO> getSprintById(@PathVariable Long id) {
        return ResponseEntity.ok(sprintBacklogService.getSprintById(id));
    }

    // ✅ Supprimer un sprint
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSprint(@PathVariable Long id) {
        sprintBacklogService.deleteSprint(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Ajouter une UserStory dans un sprint
    @PostMapping("/{sprintId}/user-stories/{userStoryId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER')")
    public ResponseEntity<SprintBacklogDTO> addUserStoryToSprint(@PathVariable Long sprintId, @PathVariable Long userStoryId) {
        SprintBacklogDTO updated = sprintBacklogService.addUserStoryToSprint(sprintId, userStoryId);
        return ResponseEntity.ok(updated);
    }

    // ✅ Supprimer une UserStory d’un sprint
    @DeleteMapping("/{sprintId}/user-stories/{userStoryId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER')")
    public ResponseEntity<SprintBacklogDTO> removeUserStoryFromSprint(@PathVariable Long sprintId, @PathVariable Long userStoryId) {
        SprintBacklogDTO updated = sprintBacklogService.removeUserStoryFromSprint(sprintId, userStoryId);
        return ResponseEntity.ok(updated);
    }
}
