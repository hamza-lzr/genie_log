package com.example.miniprojet_genie_logiciel.controllers;

import com.example.miniprojet_genie_logiciel.dto.CreateProjectDTO;
import com.example.miniprojet_genie_logiciel.dto.ProjectDTO;
import com.example.miniprojet_genie_logiciel.dto.UpdateProjectDTO;
import com.example.miniprojet_genie_logiciel.entities.ProductBacklog;
import com.example.miniprojet_genie_logiciel.entities.Project;
import com.example.miniprojet_genie_logiciel.mapper.ProjectMapper;
import com.example.miniprojet_genie_logiciel.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    // 🔹 CREATE
    @PostMapping
    @PreAuthorize("hasRole('PRODUCT_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<ProjectDTO> createProject(@RequestBody CreateProjectDTO dto) {
        Project project = projectMapper.fromCreateDTO(dto);
        Project saved = projectService.save(project);
        return ResponseEntity.ok(projectMapper.toDTO(saved));
    }

    // 🔹 READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long id) {
        Project project = projectService.findById(id);
        return ResponseEntity.ok(projectMapper.toDTO(project));
    }

    // 🔹 READ ALL
    @GetMapping
    public List<ProjectDTO> getAllProjects() {
        return projectService.findAll().stream()
                .map(projectMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 🔹 UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('PRODUCT_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody UpdateProjectDTO dto) {
        Project project = projectService.findById(id);
        projectMapper.updateProjectFromDTO(dto, project);
        Project updated = projectService.save(project);
        return ResponseEntity.ok(projectMapper.toDTO(updated));
    }

    // 🔹 DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // 🔸 CREATE BACKLOG
    @PostMapping("/{id}/backlog")
    @PreAuthorize("hasRole('PRODUCT_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<ProductBacklog> createBacklog(@PathVariable Long id, @RequestBody ProductBacklog backlog) {
        return ResponseEntity.ok(projectService.createBacklog(id, backlog));
    }

    // 🔸 GET BACKLOG
    @GetMapping("/{id}/backlog")
    public ResponseEntity<ProductBacklog> getBacklog(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getBacklog(id));
    }

    // 🔸 DELETE BACKLOG
    @DeleteMapping("/{id}/backlog")
    @PreAuthorize("hasRole('PRODUCT_OWNER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBacklog(@PathVariable Long id) {
        projectService.deleteBacklog(id);
        return ResponseEntity.noContent().build();
    }
}
