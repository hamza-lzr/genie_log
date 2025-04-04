package com.example.miniprojet_genie_logiciel.controllers;

import com.example.miniprojet_genie_logiciel.entities.*;
import com.example.miniprojet_genie_logiciel.services.UserStoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-stories")
@RequiredArgsConstructor
public class UserStoryController {

    private final UserStoryService userStoryService;

    @PostMapping
    public ResponseEntity<UserStory> createUserStory(@Valid @RequestBody UserStory userStory) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userStoryService.addUserStory(userStory));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserStory> getUserStoryById(@PathVariable Long id) {
        return ResponseEntity.ok(userStoryService.getUserStoryById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserStory>> getAllUserStories() {
        return ResponseEntity.ok(userStoryService.getAllUserStories());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserStory> updateUserStory(
            @PathVariable Long id,
            @Valid @RequestBody UserStory userStory
    ) {
        return ResponseEntity.ok(userStoryService.updateUserStory(id, userStory));
    }

    @PatchMapping("/{id}/epic")
    public ResponseEntity<UserStory> linkToEpic(
            @PathVariable Long id,
            @RequestParam Long epicId
    ) {
        return ResponseEntity.ok(userStoryService.linkUserStoryToEpic(id, epicId));
    }

    @PatchMapping("/{id}/acceptance-criteria")
    public ResponseEntity<UserStory> setAcceptanceCriteria(
            @PathVariable Long id,
            @RequestParam String criteria
    ) {
        return ResponseEntity.ok(userStoryService.setAcceptanceCriteria(id, criteria));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserStory> updateStatus(
            @PathVariable Long id,
            @RequestParam Status status
    ) {
        return ResponseEntity.ok(userStoryService.updateUserStoryStatus(id, status));
    }



    @GetMapping("/product-backlogs/{backlogId}/prioritized")
    public ResponseEntity<List<UserStory>> getPrioritizedStories(@PathVariable Long backlogId) {
        return ResponseEntity.ok(userStoryService.getPrioritizedUserStories(backlogId));
    }

    // ==== Suppression ====
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserStory(@PathVariable Long id) {
        userStoryService.deleteUserStory(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}/status")
    public ResponseEntity<UserStory> updateUserStoryStatus(@PathVariable Long id, @RequestParam Status status) {
        return ResponseEntity.ok(userStoryService.updateUserStoryStatus(id, status));
    }

    @PutMapping("/{id}/priority")
    public ResponseEntity<UserStory> updateUserStoryPriority(@PathVariable Long id, @RequestParam Priority priority) {
        return ResponseEntity.ok(userStoryService.updateUserStoryPriority(id, priority));

    }
}