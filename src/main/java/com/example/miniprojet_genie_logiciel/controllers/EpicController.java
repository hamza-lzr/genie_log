package com.example.miniprojet_genie_logiciel.controllers;

import com.example.miniprojet_genie_logiciel.entities.Epic;
import com.example.miniprojet_genie_logiciel.entities.UserStory;
import com.example.miniprojet_genie_logiciel.services.EpicService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/epics")
@RequiredArgsConstructor
public class EpicController {

    private final EpicService epicService;

    // Récupérer tous les epics
    @GetMapping
    public ResponseEntity<List<Epic>> getAllEpics() {
        List<Epic> epics = epicService.getAllEpics();
        return ResponseEntity.ok(epics);
    }

    // Récupérer un epic par son id
    @GetMapping("/{id}")
    public ResponseEntity<Epic> getEpicById(@PathVariable Long id) {
        return epicService.getEpicById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Créer un nouvel epic
    @PostMapping
    public ResponseEntity<Epic> createEpic(@RequestBody Epic epic) {
        Epic created = epicService.createEpic(epic);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Mettre à jour un epic existant
    @PutMapping("/{id}")
    public ResponseEntity<Epic> updateEpic(@PathVariable Long id, @RequestBody Epic epic) {
        // Affectation de l'id du chemin à l'epic pour assurer la cohérence
        epic.setId(id);
        Epic updated = epicService.updateEpic(epic);
        return ResponseEntity.ok(updated);
    }

    // Supprimer un epic
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEpic(@PathVariable Long id) {
        try {
            epicService.deleteEpic(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Lier une User Story à un epic
    @PostMapping("/{epicId}/userstories")
    public ResponseEntity<Epic> addUserStoryToEpic(@PathVariable Long epicId,
                                                   @RequestBody UserStory userStory) {
        try {
            Epic updated = epicService.addUserStoryToEpic(epicId, userStory);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Visualiser les User Stories liées à un epic
    @GetMapping("/{epicId}/userstories")
    public ResponseEntity<List<UserStory>> getUserStoriesByEpic(@PathVariable Long epicId) {
        try {
            List<UserStory> userStories = epicService.getUserStoriesByEpic(epicId);
            return ResponseEntity.ok(userStories);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
