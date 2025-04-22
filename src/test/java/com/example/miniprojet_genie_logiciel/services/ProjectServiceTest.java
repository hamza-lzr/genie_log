package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.entities.ProductBacklog;
import com.example.miniprojet_genie_logiciel.entities.Project;
import com.example.miniprojet_genie_logiciel.repository.ProductBacklogRepository;
import com.example.miniprojet_genie_logiciel.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @InjectMocks
    private ProjectService projectService;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveProject_success() {
        Project project = new Project();
        when(projectRepository.save(project)).thenReturn(project);

        Project result = projectService.save(project);
        assertEquals(project, result);
        verify(projectRepository).save(project);
    }

    @Test
    void findById_success() {
        Project project = new Project();
        project.setId(1L);
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Project result = projectService.findById(1L);
        assertEquals(project, result);
    }

    @Test
    void findById_notFound_throwsException() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> projectService.findById(1L));
    }

    @Test
    void findAllProjects_success() {
        List<Project> projects = List.of(new Project(), new Project());
        when(projectRepository.findAll()).thenReturn(projects);

        List<Project> result = projectService.findAll();
        assertEquals(2, result.size());
    }

    @Test
    void deleteById_success() {
        projectService.deleteById(1L);
        verify(projectRepository).deleteById(1L);
    }

    @Test
    void createBacklog_success() {
        Project project = new Project();
        project.setId(1L);
        ProductBacklog backlog = new ProductBacklog();

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(productBacklogRepository.save(backlog)).thenReturn(backlog);
        when(projectRepository.save(project)).thenReturn(project);

        ProductBacklog result = projectService.createBacklog(1L, backlog);

        assertEquals(backlog, result);
        assertEquals(backlog, project.getProductBacklog());
        verify(productBacklogRepository).save(backlog);
        verify(projectRepository).save(project);
    }

    @Test
    void getBacklog_success() {
        ProductBacklog backlog = new ProductBacklog();
        Project project = new Project();
        project.setProductBacklog(backlog);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProductBacklog result = projectService.getBacklog(1L);
        assertEquals(backlog, result);
    }

    @Test
    void deleteBacklog_success() {
        ProductBacklog backlog = new ProductBacklog();
        backlog.setId(2L);
        Project project = new Project();
        project.setProductBacklog(backlog);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        projectService.deleteBacklog(1L);

        assertNull(project.getProductBacklog());
        verify(projectRepository).save(project);
        verify(productBacklogRepository).deleteById(2L);
    }

    @Test
    void deleteBacklog_noBacklog_nothingHappens() {
        Project project = new Project();
        project.setId(1L);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        projectService.deleteBacklog(1L);

        verify(projectRepository).save(project);
        verify(productBacklogRepository, never()).deleteById(any());
    }
}

