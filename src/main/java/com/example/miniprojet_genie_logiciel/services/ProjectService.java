package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.entities.Project;
import com.example.miniprojet_genie_logiciel.entities.ProductBacklog;
import com.example.miniprojet_genie_logiciel.repository.ProjectRepository;
import com.example.miniprojet_genie_logiciel.repository.ProductBacklogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProductBacklogRepository productBacklogRepository;

    public Project save(Project project) {
        return projectRepository.save(project);
    }

    public Project findById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet introuvable avec l'id : " + id));
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public void deleteById(Long id) {
        projectRepository.deleteById(id);
    }

    // Gérer le ProductBacklog du projet

    public ProductBacklog getBacklog(Long projectId) {
        Project project = findById(projectId);
        return project.getProductBacklog();
    }

    public ProductBacklog createBacklog(Long projectId, ProductBacklog backlog) {
        Project project = findById(projectId);
        backlog = productBacklogRepository.save(backlog);
        project.setProductBacklog(backlog);
        projectRepository.save(project); // mettre à jour la relation
        return backlog;
    }

    public void deleteBacklog(Long projectId) {
        Project project = findById(projectId);
        ProductBacklog backlog = project.getProductBacklog();
        if (backlog != null) {
            project.setProductBacklog(null);
            projectRepository.save(project);
            productBacklogRepository.deleteById(backlog.getId());
        }
    }
}
