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
        // Vérification de l'existence du backlog
        if (!productBacklogService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        productBacklog.setId(id);
        ProductBacklog updated = productBacklogService.updateProductBacklog(productBacklog);
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
    @PostMapping("/{backlogId}/epics")
    public ResponseEntity<ProductBacklog> addEpic(@PathVariable Long backlogId,
                                                  @RequestBody Epic epic) {
        try {
            ProductBacklog updated = productBacklogService.addEpicToProductBacklog(backlogId, epic);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer un Epic d'un ProductBacklog
    @DeleteMapping("/{backlogId}/epics")
    public ResponseEntity<ProductBacklog> removeEpic(@PathVariable Long backlogId,
                                                     @RequestBody Epic epic) {
        try {
            ProductBacklog updated = productBacklogService.removeEpicFromProductBacklog(backlogId, epic);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Ajouter une UserStory à un ProductBacklog
    @PostMapping("/{backlogId}/userstories")
    public ResponseEntity<ProductBacklog> addUserStory(@PathVariable Long backlogId,
                                                       @RequestBody UserStory userStory) {
        try {
            ProductBacklog updated = productBacklogService.addUserStoryToProductBacklog(backlogId, userStory);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Supprimer une UserStory d'un ProductBacklog
    @DeleteMapping("/{backlogId}/userstories")
    public ResponseEntity<ProductBacklog> removeUserStory(@PathVariable Long backlogId,
                                                          @RequestBody UserStory userStory) {
        try {
            ProductBacklog updated = productBacklogService.removeUserStoryFromProductBacklog(backlogId, userStory);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
/*
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

 */
}
