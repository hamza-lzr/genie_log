package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.entities.ProductBacklog;
import com.example.miniprojet_genie_logiciel.entities.Project;
import com.example.miniprojet_genie_logiciel.repository.EpicRepository;
import com.example.miniprojet_genie_logiciel.repository.ProductBacklogRepository;
import com.example.miniprojet_genie_logiciel.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @Mock
    private EpicRepository epicRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project project;
    private ProductBacklog productBacklog;

    @BeforeEach
    void setUp() {
        project = new Project();
        project.setId(1L);
        project.setTitle("Test Project");
        project.setStatus("In Progress");
        project.setStartDate(new Date());
        project.setEndDate(new Date());

        productBacklog = new ProductBacklog();
        productBacklog.setId(1L);
        productBacklog.setName("Test Product Backlog");
    }

    @Test
    void testGetProjectById() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        Optional<Project> found = projectService.getProjectById(1L);

        assertTrue(found.isPresent());
        assertEquals("Test Project", found.get().getTitle());
    }

    @Test
    void testGetAllProjects() {
        when(projectRepository.findAll()).thenReturn(List.of(project));

        List<Project> projects = projectService.getAllProjects();

        assertFalse(projects.isEmpty());
        assertEquals(1, projects.size());
        assertEquals("Test Project", projects.get(0).getTitle());
    }

    @Test
    void testSaveProject() {
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        Project saved = projectService.saveProject(project);

        assertNotNull(saved);
        assertEquals("Test Project", saved.getTitle());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void testUpdateProject_Success() {
        Project updatedProject = new Project();
        updatedProject.setTitle("Updated Project");
        updatedProject.setStatus("Completed");
        updatedProject.setStartDate(new Date());
        updatedProject.setEndDate(new Date());

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(updatedProject);

        Project result = projectService.updateProject(updatedProject, 1L);

        assertNotNull(result);
        assertEquals("Updated Project", result.getTitle());
        assertEquals("Completed", result.getStatus());
    }

    @Test
    void testUpdateProject_NotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        Project result = projectService.updateProject(project, 1L);

        assertNull(result);
    }

    @Test
    void testDeleteProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        doNothing().when(projectRepository).delete(project);

        projectService.deleteProject(1L);

        verify(projectRepository, times(1)).delete(project);
    }

    @Test
    void testLinkProductBacklog_Success() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(productBacklog));
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProductBacklog result = projectService.linkProductBacklog(1L, 1L);

        assertNotNull(result);
        assertEquals(project, result.getProject());
        assertEquals(productBacklog, project.getProductBacklog());
    }

    @Test
    void testLinkProductBacklog_NotFound() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.empty());
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProductBacklog result = projectService.linkProductBacklog(1L, 1L);

        assertNull(result);
    }

    @Test
    void testUnlinkProductBacklog() {
        project.setProductBacklog(productBacklog);
        productBacklog.setProject(project);

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(productBacklog));

        projectService.unlinkProductBacklog(1L, 1L);

        assertNull(project.getProductBacklog());
        assertNull(productBacklog.getProject());
    }
}