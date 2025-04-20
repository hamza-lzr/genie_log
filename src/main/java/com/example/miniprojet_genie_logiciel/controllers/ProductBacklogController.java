package com.example.miniprojet_genie_logiciel.controllers;

import com.example.miniprojet_genie_logiciel.dto.*;
import com.example.miniprojet_genie_logiciel.entities.Status;
import com.example.miniprojet_genie_logiciel.services.ProductBacklogService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product-backlogs")
@RequiredArgsConstructor
public class ProductBacklogController {

    private final ProductBacklogService backlogService;

    // === CRUD PRODUCT BACKLOG ===

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER')")
    public ProductBacklogDTO createBacklog(@RequestBody ProductBacklogDTO dto) {
        return backlogService.createBacklog(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER')")
    public List<ProductBacklogDTO> getAllBacklogs() {
        return backlogService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER')")
    public Optional<ProductBacklogDTO> getBacklogById(@PathVariable Long id) {
        return backlogService.findById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER')")
    public ProductBacklogDTO updateBacklog(@PathVariable Long id, @RequestBody ProductBacklogDTO dto) {
        return backlogService.updateBacklog(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBacklog(@PathVariable Long id) {
        backlogService.deleteById(id);
    }

    // === EPICS ===

    @PostMapping("/{backlogId}/epics")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER')")
    public EpicDTO createEpic(@PathVariable Long backlogId, @RequestBody CreateEpicDTO dto) {
        return backlogService.createEpic(backlogId, dto);
    }

    @DeleteMapping("/{backlogId}/epics/{epicId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER')")
    public void deleteEpic(@PathVariable Long backlogId, @PathVariable Long epicId) {
        backlogService.deleteEpic(backlogId, epicId);
    }

    // === USER STORIES ===

    @PostMapping("/{backlogId}/user-stories")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER')")
    public UserStoryDTO createUserStory(@PathVariable Long backlogId, @RequestBody CreateUserStoryDTO dto) {
        return backlogService.createUserStory(backlogId, dto);
    }

    @DeleteMapping("/{backlogId}/user-stories/{storyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER')")
    public void deleteUserStory(@PathVariable Long backlogId, @PathVariable Long storyId) {
        backlogService.deleteUserStory(backlogId, storyId);
    }

    // === MOSCOW PRIORITIZATION ===

    @GetMapping("/{backlogId}/prioritize")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER')")
    public List<UserStoryDTO> prioritizeUserStories(@PathVariable Long backlogId) {
        return backlogService.prioritizeUserStories(backlogId);
    }

    // === FILTERING ===

    @GetMapping("/{backlogId}/filter")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER')")
    public List<UserStoryDTO> filterUserStories(
            @PathVariable Long backlogId,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String keyword
    ) {
        PriorityDTO priorityDTO = null;
        if (priority != null && !priority.isBlank()) {
            priorityDTO = new PriorityDTO(null, priority, null);
        }
        return backlogService.filterUserStories(backlogId, status, priorityDTO, keyword);
    }

}
