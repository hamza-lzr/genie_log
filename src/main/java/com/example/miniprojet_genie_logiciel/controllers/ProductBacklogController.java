package com.example.miniprojet_genie_logiciel.controllers;

import com.example.miniprojet_genie_logiciel.entities.ProductBacklog;
import com.example.miniprojet_genie_logiciel.entities.Epic;
import com.example.miniprojet_genie_logiciel.entities.UserStory;
import com.example.miniprojet_genie_logiciel.services.ProductBacklogService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-backlogs")
@RequiredArgsConstructor
public class ProductBacklogController {

    private final ProductBacklogService productBacklogService;

    // Récupérer tous les ProductBacklogs
    @GetMapping
    public ResponseEntity<List<ProductBacklog>> getAll() {
        List<ProductBacklog> productBacklogs = productBacklogService.findAll();
        return ResponseEntity.ok(productBacklogs);
    }

    // Récupérer un ProductBacklog par son id
    @GetMapping("/{id}")
    public ResponseEntity<ProductBacklog> getById(@PathVariable Long id) {
        return productBacklogService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Créer un nouveau ProductBacklog
    @PostMapping
    public ResponseEntity<ProductBacklog> create(@RequestBody ProductBacklog productBacklog) {
        ProductBacklog saved = productBacklogService.saveProductBacklog(productBacklog);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Mettre à jour un ProductBacklog existant
    @PutMapping("/{id}")
    public ResponseEntity<ProductBacklog> update(@PathVariable Long id,
                                                 @RequestBody ProductBacklog productBacklog) {
        ProductBacklog updated = productBacklogService.updateProductBacklog(productBacklog, id);
        return ResponseEntity.ok(updated);
    }

    // Supprimer un ProductBacklog par son id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            productBacklogService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Ajouter un Epic à un ProductBacklog
    @PostMapping("/{backlogId}/link-epic/{epicId}")
    public ResponseEntity<ProductBacklog> addEpic(@PathVariable Long backlogId,
                                                  @PathVariable Long epicId) {
        try {
            ProductBacklog updated = productBacklogService.addEpicToProductBacklog(backlogId, epicId);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer un Epic d'un ProductBacklog
    @DeleteMapping("/{backlogId}/unlink-epic/{epicId}")
    public ResponseEntity<ProductBacklog> removeEpic(@PathVariable Long backlogId,
                                                     @PathVariable Long epicId) {
        try {
            ProductBacklog updated = productBacklogService.removeEpicFromProductBacklog(backlogId, epicId);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Ajouter une UserStory à un ProductBacklog
    @PostMapping("/{backlogId}/link-us/{usId}")
    public ResponseEntity<ProductBacklog> addUserStory(@PathVariable Long backlogId,
                                                       @PathVariable Long usId) {
        try {
            ProductBacklog updated = productBacklogService.addUserStoryToProductBacklog(backlogId, usId);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer une UserStory d'un ProductBacklog
    @DeleteMapping("/{backlogId}/unlink-us/{usId}")
    public ResponseEntity<ProductBacklog> removeUserStory(@PathVariable Long backlogId,
                                                          @PathVariable Long usId) {
        try {
            ProductBacklog updated = productBacklogService.removeUserStoryFromProductBacklog(backlogId, usId);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Récupérer les UserStories triées par priorité (méthode MoSCoW)
    @GetMapping("/{backlogId}/userstories/prioritized")
    public ResponseEntity<List<UserStory>> getPrioritizedUserStories(@PathVariable Long backlogId) {
        try {

            List<UserStory> prioritized = productBacklogService.prioritizeUserStoriesMoscow(backlogId);
            return ResponseEntity.ok(prioritized);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
