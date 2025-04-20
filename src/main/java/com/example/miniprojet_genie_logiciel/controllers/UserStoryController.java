package com.example.miniprojet_genie_logiciel.controllers;

import com.example.miniprojet_genie_logiciel.dto.CreateUserStoryDTO;
import com.example.miniprojet_genie_logiciel.dto.UpdateUserStoryDTO;
import com.example.miniprojet_genie_logiciel.dto.UserStoryDTO;
import com.example.miniprojet_genie_logiciel.services.UserStoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/userstories")
@RequiredArgsConstructor
public class UserStoryController {

    private final UserStoryService userStoryService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER')")
    public UserStoryDTO create(@RequestBody CreateUserStoryDTO dto) {
        return userStoryService.createUserStory(dto);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER')")
    public List<UserStoryDTO> findAll() {
        return userStoryService.getAllUserStories();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER', 'DEVELOPER')")
    public UserStoryDTO findById(@PathVariable Long id) {
        return userStoryService.getUserStoryById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER')")
    public UserStoryDTO update(@PathVariable Long id, @RequestBody UpdateUserStoryDTO dto) {
        return userStoryService.updateUserStory(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER')")
    public void delete(@PathVariable Long id) {
        userStoryService.deleteUserStory(id);
    }

    @GetMapping("/epic/{epicId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER', 'DEVELOPER')")
    public List<UserStoryDTO> findByEpic(@PathVariable Long epicId) {
        return userStoryService.getUserStoriesByEpic(epicId);
    }

    @GetMapping("/sprint/{sprintId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PRODUCT_OWNER', 'SCRUM_MASTER', 'DEVELOPER')")
    public List<UserStoryDTO> findBySprint(@PathVariable Long sprintId) {
        return userStoryService.getUserStoriesBySprintBacklog(sprintId);
    }
}

