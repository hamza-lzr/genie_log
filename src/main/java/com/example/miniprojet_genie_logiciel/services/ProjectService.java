package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.entities.ProductBacklog;
import com.example.miniprojet_genie_logiciel.entities.Project;
import com.example.miniprojet_genie_logiciel.repository.EpicRepository;
import com.example.miniprojet_genie_logiciel.repository.ProductBacklogRepository;
import com.example.miniprojet_genie_logiciel.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    //injections des dependances:
    private final ProjectRepository projectRepository;
    private final ProductBacklogRepository productbacklogrepository;

    public ProjectService(ProjectRepository projectRepository, ProductBacklogRepository productbacklogrepository, EpicRepository epicRepository) {
        this.projectRepository = projectRepository;
        this.productbacklogrepository = productbacklogrepository;
    }

    //CRUD
    public Optional<Project> getProjectById(long id) {
        return projectRepository.findById(id);
    }
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }
    public Project updateProject(Project project, long id) {
        Optional<Project> existingProj = getProjectById(id);
        if (existingProj.isPresent()) {
            Project exProj = existingProj.get();
            exProj.setTitle(project.getTitle());
            exProj.setStatus(project.getStatus());
            exProj.setStartDate(project.getStartDate());
            exProj.setEndDate(project.getEndDate());
            return projectRepository.save(exProj);
        }

        else return null;
    }
    public void deleteProject(long id) {
        Optional<Project> existingProj = getProjectById(id);
        existingProj.ifPresent(projectRepository::delete);

    }

    //link unlink PBACKLOG methods
    public ProductBacklog linkProductBacklog(Long pbId, Long projId) {
        Optional<ProductBacklog> exisPb = productbacklogrepository.findById(pbId);
        Optional<Project> existingProj = getProjectById(projId);
        if (exisPb.isPresent() && existingProj.isPresent()) {
            Project exProj = existingProj.get();
            ProductBacklog pb = exisPb.get();
            exProj.setProductBacklog(pb);
            pb.setProject(exProj);
            return pb;

        }
        else return null;

    }
    public void unlinkProductBacklog(Long pbId, Long projId) {
        Optional<Project> existingProj = getProjectById(projId);
        Optional<ProductBacklog> existingProjBacklog = productbacklogrepository.findById(pbId);
        if (existingProj.isPresent() && existingProjBacklog.isPresent()) {
            Project exProj = existingProj.get();
            ProductBacklog pb = existingProjBacklog.get();
            if(exProj.getProductBacklog().equals(pb)) {
                exProj.setProductBacklog(null);
                pb.setProject(null);

            }

        }

    }


}
