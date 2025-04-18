package com.example.miniprojet_genie_logiciel.controllers;

import com.example.miniprojet_genie_logiciel.dto.ProductBacklogDTO;
import com.example.miniprojet_genie_logiciel.dto.ProjectDTO;
import com.example.miniprojet_genie_logiciel.entities.ProductBacklog;
import com.example.miniprojet_genie_logiciel.entities.Project;
import com.example.miniprojet_genie_logiciel.mapper.ProjectMapper;
import com.example.miniprojet_genie_logiciel.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("api/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;
    private final ProjectMapper projectMapper;

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getProjects() {
        List<ProjectDTO> projectDTOs = projectService.getAllProjects();
        return ResponseEntity.ok(projectDTOs);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long id) {
        try {
            ProjectDTO projectDTO = projectService.getProjectById(id);
            return ResponseEntity.ok(projectDTO);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody ProjectDTO projectDTO) {
        ProjectDTO savedProject = projectService.saveProject(projectDTO);
        return ResponseEntity.ok(savedProject);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody ProjectDTO projectDTO) {
        ProjectDTO updatedProject = projectService.updateProject(projectDTO, id);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("link-pb/{pjId}/{pbId}")
    public ResponseEntity<ProductBacklogDTO> linkProjectToPB(@PathVariable Long pjId, @PathVariable Long pbId) {
        ProductBacklogDTO productBacklogDTO = projectService.linkProductBacklog(pbId, pjId);
        return ResponseEntity.ok(productBacklogDTO);
    }

    @DeleteMapping("unlink-pb/{pjId}/{pbId}")
    public ResponseEntity<Void> unlinkProjectFromPB(@PathVariable Long pjId, @PathVariable Long pbId) {
        projectService.unlinkProductBacklog(pbId, pjId);
        return ResponseEntity.noContent().build();
    }



}


