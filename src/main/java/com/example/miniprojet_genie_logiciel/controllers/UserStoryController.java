package com.example.miniprojet_genie_logiciel.controllers;

import com.example.miniprojet_genie_logiciel.dto.UserStoryDTO;
import com.example.miniprojet_genie_logiciel.entities.*;
import com.example.miniprojet_genie_logiciel.mapper.UserStoryMapper;
import com.example.miniprojet_genie_logiciel.services.UserStoryService;
import com.example.miniprojet_genie_logiciel.dto.TaskDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-stories")
@RequiredArgsConstructor
public class UserStoryController {

    private final UserStoryService userStoryService;
    private final UserStoryMapper userStoryMapper;

    @PreAuthorize("hasRole('PRODUCT_OWNER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserStoryDTO> createUserStory(@Valid @RequestBody UserStoryDTO userStoryDTO) {
        UserStoryDTO created = userStoryService.addUserStory(userStoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }

    @PreAuthorize("hasAnyRole('PRODUCT_OWNER', 'SCRUM_MASTER', 'DEVELOPER', 'ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserStoryDTO> getUserStoryById(@PathVariable Long id) {
        UserStoryDTO userStoryDTO = userStoryService.getUserStoryById(id);
        return ResponseEntity.ok(userStoryDTO);
    }

    @PreAuthorize("hasAnyRole('PRODUCT_OWNER', 'SCRUM_MASTER', 'DEVELOPER', 'ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserStoryDTO>> getAllUserStories() {
        List<UserStoryDTO> userStoryDTOs = userStoryService.getAllUserStories();
        return ResponseEntity.ok(userStoryDTOs);
    }

    @PreAuthorize("hasRole('PRODUCT_OWNER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserStoryDTO> updateUserStory(
            @PathVariable Long id,
            @Valid @RequestBody UserStoryDTO userStoryDTO
    ) {
        UserStoryDTO updated = userStoryService.updateUserStory(id, userStoryDTO);
        return ResponseEntity.ok(updated);
    }


    @PostMapping("/link-epic/{usId}/{epId}")
    public ResponseEntity<UserStoryDTO> linkToEpic(
            @PathVariable Long usId,
            @PathVariable Long epId
    ) {
        UserStoryDTO userStoryDTO = userStoryService.linkUserStoryToEpic(usId, epId);
        return ResponseEntity.ok(userStoryDTO);
    }

    @PostMapping("/unlink-epic/{usId}/{epId}")
    public ResponseEntity<UserStoryDTO> unlinkToEpic(
            @PathVariable Long usId,
            @PathVariable Long epId
    ){
        UserStoryDTO userStoryDTO = userStoryService.unlinkUserStoryFromEpic(usId, epId);
        return ResponseEntity.ok(userStoryDTO);
    }

    @PatchMapping("/{id}/acceptance-criteria")
    public ResponseEntity<UserStoryDTO> setAcceptanceCriteria(
            @PathVariable Long id,
            @RequestParam String criteria
    ) {
        UserStoryDTO userStoryDTO = userStoryService.setAcceptanceCriteria(id, criteria);
        return ResponseEntity.ok(userStoryDTO);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserStoryDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam Status status
    ) {
        UserStoryDTO userStoryDTO = userStoryService.updateUserStoryStatus(id, status);
        return ResponseEntity.ok(userStoryDTO);
    }



    @GetMapping("/product-backlogs/{backlogId}/prioritized")
    public ResponseEntity<List<UserStoryDTO>> getPrioritizedStories(@PathVariable Long backlogId) {
        List<UserStoryDTO> userStoryDTOs = userStoryService.getPrioritizedUserStories(backlogId);
        return ResponseEntity.ok(userStoryDTOs);
    }

    // ==== Suppression ====
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserStory(@PathVariable Long id) {
        userStoryService.deleteUserStory(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}/status")
    public ResponseEntity<UserStoryDTO> updateUserStoryStatus(@PathVariable Long id, @RequestParam Status status) {
        return ResponseEntity.ok(userStoryService.updateUserStoryStatus(id, status));
    }

    @PutMapping("/{id}/priority")
    public ResponseEntity<UserStoryDTO> updateUserStoryPriority(@PathVariable Long id, @RequestParam Priority priority) {
        return ResponseEntity.ok(userStoryService.updateUserStoryPriority(id, priority));
    }

    //Tasks
    @PostMapping("/{userStoryId}/tasks")
    public ResponseEntity<TaskDTO> addTaskToUserStory(
            @PathVariable Long userStoryId,
            @Valid @RequestBody TaskDTO taskDTO
    ) {
        TaskDTO createdTask = userStoryService.addTaskToUserStory(userStoryId, taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @DeleteMapping("/{userStoryId}/tasks/{taskId}")
    public ResponseEntity<Void> deleteTaskFromUserStory(
            @PathVariable Long userStoryId,
            @PathVariable Long taskId
    ) {
        userStoryService.deleteTaskFromUserStory(userStoryId, taskId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/tasks/{taskId}/status")
    public ResponseEntity<TaskDTO> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestParam String status
    ) {
        TaskDTO updatedTask = userStoryService.updateTaskStatus(taskId, status);
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/{userStoryId}/tasks")
    public ResponseEntity<List<TaskDTO>> getTasksForUserStory(@PathVariable Long userStoryId) {
        return ResponseEntity.ok(userStoryService.getTasksForUserStory(userStoryId));
    }





}

