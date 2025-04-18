package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.dto.ProductBacklogDTO;
import com.example.miniprojet_genie_logiciel.dto.ProjectDTO;
import com.example.miniprojet_genie_logiciel.entities.ProductBacklog;
import com.example.miniprojet_genie_logiciel.entities.Project;
import com.example.miniprojet_genie_logiciel.mapper.ProductBacklogMapper;
import com.example.miniprojet_genie_logiciel.mapper.ProjectMapper;
import com.example.miniprojet_genie_logiciel.repository.ProductBacklogRepository;
import com.example.miniprojet_genie_logiciel.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
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
    private ProjectMapper projectMapper;

    @Mock
    private ProductBacklogMapper productBacklogMapper;

    @InjectMocks
    private ProjectService projectService;

    private Project project;
    private ProjectDTO projectDTO;
    private ProductBacklog productBacklog;
    private ProductBacklogDTO productBacklogDTO;

    @BeforeEach
    void setUp() {
        project = new Project();
        project.setId(1L);
        project.setTitle("Test Project");
        project.setStatus("In Progress");
        project.setStartDate(java.sql.Date.valueOf(LocalDate.now()));
        project.setEndDate(java.sql.Date.valueOf(LocalDate.now().plusMonths(1)));

        projectDTO = new ProjectDTO();
        projectDTO.setId(1L);
        projectDTO.setName("Test Project");
        projectDTO.setDescription("Test Description");
        projectDTO.setStatus("In Progress");
        projectDTO.setStartDate(LocalDate.now());
        projectDTO.setEndDate(LocalDate.now().plusMonths(1));

        productBacklog = new ProductBacklog();
        productBacklog.setId(1L);
        productBacklog.setName("Test Product Backlog");

        productBacklogDTO = new ProductBacklogDTO();
        productBacklogDTO.setId(1L);
        productBacklogDTO.setName("Test Product Backlog");
    }

    @Test
    void testGetProjectById() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectMapper.toDto(project)).thenReturn(projectDTO);

        ProjectDTO found = projectService.getProjectById(1L);

        assertNotNull(found);
        assertEquals("Test Project", found.getName());
        verify(projectMapper).toDto(project);
    }

    @Test
    void testGetAllProjects() {
        when(projectRepository.findAll()).thenReturn(List.of(project));
        when(projectMapper.toDto(project)).thenReturn(projectDTO);

        List<ProjectDTO> projects = projectService.getAllProjects();

        assertFalse(projects.isEmpty());
        assertEquals(1, projects.size());
        assertEquals("Test Project", projects.get(0).getName());
        verify(projectMapper).toDto(project);
    }

    @Test
    void testSaveProject() {
        when(projectMapper.toEntity(projectDTO)).thenReturn(project);
        when(projectRepository.save(any(Project.class))).thenReturn(project);
        when(projectMapper.toDto(project)).thenReturn(projectDTO);

        ProjectDTO saved = projectService.saveProject(projectDTO);

        assertNotNull(saved);
        assertEquals("Test Project", saved.getName());
        verify(projectMapper).toEntity(projectDTO);
        verify(projectRepository).save(any(Project.class));
        verify(projectMapper).toDto(project);
    }

    @Test
    void testUpdateProject_Success() {
        ProjectDTO updatedProjectDTO = new ProjectDTO();
        updatedProjectDTO.setName("Updated Project");
        updatedProjectDTO.setStatus("Completed");
        updatedProjectDTO.setStartDate(LocalDate.now());
        updatedProjectDTO.setEndDate(LocalDate.now().plusMonths(1));

        Project updatedProject = new Project();
        updatedProject.setTitle("Updated Project");
        updatedProject.setStatus("Completed");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(updatedProject);
        when(projectMapper.toDto(updatedProject)).thenReturn(updatedProjectDTO);

        ProjectDTO result = projectService.updateProject(updatedProjectDTO, 1L);

        assertNotNull(result);
        assertEquals("Updated Project", result.getName());
        assertEquals("Completed", result.getStatus());
        verify(projectMapper).toDto(updatedProject);
    }

    @Test
    void testUpdateProject_NotFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());

        ProjectDTO result = projectService.updateProject(projectDTO, 1L);

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
        when(productBacklogMapper.toDto(any(ProductBacklog.class))).thenReturn(productBacklogDTO);

        ProductBacklogDTO result = projectService.linkProductBacklog(1L, 1L);

        assertNotNull(result);
        assertEquals(productBacklogDTO.getId(), result.getId());
        assertEquals(productBacklogDTO.getName(), result.getName());
        verify(productBacklogMapper).toDto(any(ProductBacklog.class));
    }

    @Test
    void testLinkProductBacklog_NotFound() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.empty());
        when(projectRepository.findById(1L)).thenReturn(Optional.of(project));

        ProductBacklogDTO result = projectService.linkProductBacklog(1L, 1L);

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