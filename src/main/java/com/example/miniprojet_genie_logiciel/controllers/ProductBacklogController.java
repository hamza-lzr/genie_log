package com.example.miniprojet_genie_logiciel.controllers;

import com.example.miniprojet_genie_logiciel.entities.*;
import com.example.miniprojet_genie_logiciel.services.ProductBacklogService;
import com.example.miniprojet_genie_logiciel.dto.*;
import com.example.miniprojet_genie_logiciel.mapper.ProductBacklogMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product-backlog")
@RequiredArgsConstructor
public class ProductBacklogController {

    private final ProductBacklogService productBacklogService;
    private final ProductBacklogMapper productBacklogMapper;

    @GetMapping
    public ResponseEntity<List<ProductBacklogDTO>> getAll() {
        List<ProductBacklogDTO> productBacklogs = productBacklogService.findAll();
        return ResponseEntity.ok(productBacklogs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductBacklogDTO> getById(@PathVariable Long id) {
        return productBacklogService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProductBacklogDTO> create(@RequestBody ProductBacklogDTO productBacklogDTO) {
        ProductBacklogDTO saved = productBacklogService.saveProductBacklog(productBacklogDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductBacklogDTO> update(@PathVariable Long id,
                                                 @RequestBody ProductBacklogDTO productBacklogDTO) {
        ProductBacklogDTO updated = productBacklogService.updateProductBacklog(productBacklogDTO, id);
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
    public ResponseEntity<ProductBacklogDTO> addEpic(@PathVariable Long backlogId,
                                                  @PathVariable Long epicId) {
        try {
            ProductBacklogDTO updated = productBacklogService.addEpicToProductBacklog(backlogId, epicId);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{backlogId}/unlink-epic/{epicId}")
    public ResponseEntity<ProductBacklogDTO> removeEpic(@PathVariable Long backlogId,
                                                     @PathVariable Long epicId) {
        try {
            ProductBacklogDTO updated = productBacklogService.removeEpicFromProductBacklog(backlogId, epicId);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Ajouter une UserStory à un ProductBacklog
    @PostMapping("/{backlogId}/link-us/{usId}")
    public ResponseEntity<ProductBacklogDTO> addUserStory(@PathVariable Long backlogId,
                                                       @PathVariable Long usId) {
        try {
            ProductBacklogDTO updated = productBacklogService.addUserStoryToProductBacklog(backlogId, usId);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{backlogId}/unlink-us/{usId}")
    public ResponseEntity<ProductBacklogDTO> removeUserStory(@PathVariable Long backlogId,
                                                          @PathVariable Long usId) {
        try {
            ProductBacklogDTO updated = productBacklogService.removeUserStoryFromProductBacklog(backlogId, usId);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Récupérer les UserStories triées par priorité (méthode MoSCoW)
    @GetMapping("/{backlogId}/user-stories/prioritized")
    public ResponseEntity<List<UserStoryDTO>> getPrioritizedUserStories(@PathVariable Long backlogId) {
        try {
            List<UserStoryDTO> prioritized = productBacklogService.prioritizeUserStoriesMoscow(backlogId);
            return ResponseEntity.ok(prioritized);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{backlogId}/user-stories/filter")
    public ResponseEntity<List<UserStoryDTO>> filterUserStories(
            @PathVariable Long backlogId,
            @RequestParam(required = false) StatusDTO status,
            @RequestParam(required = false) PriorityDTO priority,
            @RequestParam(required = false) String keyword) {

        List<UserStoryDTO> result = productBacklogService.filterUserStories(backlogId, status, priority, keyword);
        return ResponseEntity.ok(result);
    }


}

