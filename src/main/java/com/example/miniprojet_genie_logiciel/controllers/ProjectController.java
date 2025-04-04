package com.example.miniprojet_genie_logiciel.controllers;

import com.example.miniprojet_genie_logiciel.entities.ProductBacklog;
import com.example.miniprojet_genie_logiciel.entities.Project;
import com.example.miniprojet_genie_logiciel.services.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/project")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public ResponseEntity<List<Project>> getProjects() {
        return ResponseEntity.ok(projectService.getAllProjects());

    }
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Project>> getProject(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectById(id));

    }
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        return ResponseEntity.ok(projectService.saveProject(project));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project project) {
        return ResponseEntity.ok(projectService.updateProject(project, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("link-pb/{pjId}/{pbId}")
    public ResponseEntity<ProductBacklog> linkProjectToPB(@PathVariable Long pjId, @PathVariable Long pbId) {
        return ResponseEntity.ok(projectService.linkProductBacklog(pbId, pjId));

    }

    @DeleteMapping("unlink-pb/{pjId}/{pbId}")
    public ResponseEntity<Void> unlinkProjectFromPB(@PathVariable Long pjId, @PathVariable Long pbId) {
        projectService.unlinkProductBacklog(pbId, pjId);
        return ResponseEntity.noContent().build();
    }



}
