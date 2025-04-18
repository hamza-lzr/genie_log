package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.dto.ProductBacklogDTO;
import com.example.miniprojet_genie_logiciel.dto.ProjectDTO;
import com.example.miniprojet_genie_logiciel.entities.ProductBacklog;
import com.example.miniprojet_genie_logiciel.entities.Project;
import com.example.miniprojet_genie_logiciel.mapper.ProductBacklogMapper;
import com.example.miniprojet_genie_logiciel.mapper.ProjectMapper;
import com.example.miniprojet_genie_logiciel.repository.ProductBacklogRepository;
import com.example.miniprojet_genie_logiciel.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProductBacklogRepository productbacklogrepository;
    private final ProjectMapper projectMapper;
    private final ProductBacklogMapper productBacklogMapper;

    //CRUD
    public ProjectDTO getProjectById(long id) {
        return projectRepository.findById(id)
                .map(projectMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
    }

    public List<ProjectDTO> getAllProjects() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toDto)
                .toList();
    }

    public ProjectDTO saveProject(ProjectDTO projectDTO) {
        Project project = projectMapper.toEntity(projectDTO);
        Project savedProject = projectRepository.save(project);
        return projectMapper.toDto(savedProject);
    }

    public ProjectDTO updateProject(ProjectDTO projectDTO, long id) {
        Project existingProj = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
        Project projectToUpdate = projectMapper.toEntity(projectDTO);
        existingProj.setTitle(projectToUpdate.getTitle());
        existingProj.setStatus(projectToUpdate.getStatus());
        existingProj.setStartDate(projectToUpdate.getStartDate());
        existingProj.setEndDate(projectToUpdate.getEndDate());
        Project savedProject = projectRepository.save(existingProj);
        return projectMapper.toDto(savedProject);
    }
    public void deleteProject(long id) {
        Project existingProj = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + id));
        projectRepository.delete(existingProj);
    }

    //link unlink PBACKLOG methods
    public ProductBacklogDTO linkProductBacklog(Long pbId, Long projId) {
        ProductBacklog pb = productbacklogrepository.findById(pbId)
                .orElseThrow(() -> new EntityNotFoundException("ProductBacklog not found with id: " + pbId));
        Project project = projectRepository.findById(projId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projId));
        
        project.setProductBacklog(pb);
        pb.setProject(project);
        ProductBacklog savedPb = productbacklogrepository.save(pb);
        return productBacklogMapper.toDto(savedPb);
    }
    public void unlinkProductBacklog(Long pbId, Long projId) {
        Project project = projectRepository.findById(projId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projId));
        ProductBacklog pb = productbacklogrepository.findById(pbId)
                .orElseThrow(() -> new EntityNotFoundException("ProductBacklog not found with id: " + pbId));
        
        if(project.getProductBacklog() != null && project.getProductBacklog().equals(pb)) {
            project.setProductBacklog(null);
            pb.setProject(null);
            projectRepository.save(project);
            productbacklogrepository.save(pb);
        }
    }


}
