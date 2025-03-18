package com.example.miniprojet_genie_logiciel.controllers;

import com.example.miniprojet_genie_logiciel.entities.UserStory;
import com.example.miniprojet_genie_logiciel.services.UserStoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userstories")
@RequiredArgsConstructor
public class UserStoryController {

    private final UserStoryService userStoryService;

    // Ajouter une nouvelle User Story
    @PostMapping
    public ResponseEntity<UserStory> addUserStory(@RequestBody UserStory userStory) {
        UserStory added = userStoryService.addUserStory(userStory);
        return ResponseEntity.status(HttpStatus.CREATED).body(added);
    }

    // Mettre à jour une User Story existante
    @PutMapping("/{id}")
    public ResponseEntity<UserStory> updateUserStory(@PathVariable Long id,
                                                     @RequestBody UserStory userStory) {
        userStory.setId(id);
        try {
            UserStory updated = userStoryService.updateUserStory(userStory);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer une User Story
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserStory(@PathVariable Long id) {
        try {
            userStoryService.deleteUserStory(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Lier une User Story à un Epic
    @PutMapping("/{userStoryId}/epics/{epicId}")
    public ResponseEntity<UserStory> linkUserStoryToEpic(@PathVariable Long userStoryId,
                                                         @PathVariable Long epicId) {
        try {
            UserStory linked = userStoryService.linkUserStoryToEpic(userStoryId, epicId);
            return ResponseEntity.ok(linked);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Définir les critères d'acceptation pour une User Story
    @PutMapping("/{userStoryId}/acceptance-criteria")
    public ResponseEntity<UserStory> setAcceptanceCriteria(@PathVariable Long userStoryId,
                                                           @RequestParam String criteria) {
        try {
            UserStory updated = userStoryService.setAcceptanceCriteria(userStoryId, criteria);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Mettre à jour le statut d'une User Story (To Do, In Progress, Done)
    @PutMapping("/{userStoryId}/status")
    public ResponseEntity<UserStory> updateUserStoryStatus(@PathVariable Long userStoryId,
                                                           @RequestParam String status) {
        try {
            UserStory updated = userStoryService.updateUserStoryStatus(userStoryId, status);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Récupérer les User Stories priorisées dans un Product Backlog via la méthode MoSCoW
    @GetMapping("/prioritized")
    public ResponseEntity<List<UserStory>> getPrioritizedUserStories(@RequestParam Long backlogId) {
        try {
            List<UserStory> prioritized = userStoryService.prioritizeUserStories(backlogId);
            return ResponseEntity.ok(prioritized);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
