package com.example.miniprojet_genie_logiciel.services;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.miniprojet_genie_logiciel.entities.Epic;
import com.example.miniprojet_genie_logiciel.entities.Priority;
import com.example.miniprojet_genie_logiciel.entities.ProductBacklog;
import com.example.miniprojet_genie_logiciel.entities.UserStory;
import com.example.miniprojet_genie_logiciel.repository.EpicRepository;
import com.example.miniprojet_genie_logiciel.repository.ProductBacklogRepository;
import com.example.miniprojet_genie_logiciel.repository.UserStoryRepository;
import com.example.miniprojet_genie_logiciel.services.ProductBacklogService;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class ProductBacklogServiceTest {

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @Mock
    private EpicRepository epicRepository;

    @Mock
    private UserStoryRepository userStoryRepository;

    @InjectMocks
    private ProductBacklogService productBacklogService;

    private ProductBacklog mockBacklog;
    private Epic mockEpic;
    private UserStory mockUserStory;

    @BeforeEach
    void setUp() {
        mockBacklog = new ProductBacklog();
        mockEpic = new Epic();
        mockUserStory = new UserStory();

        mockBacklog.setId(1L);
        mockBacklog.setName("test backlog");
        mockEpic.setId(2L);
        mockEpic.setTitle("test epic");
        mockEpic.setDescription("test epic description");
        mockUserStory.setId(3L);
        mockUserStory.setTitle("test user story");
        mockUserStory.setGoal("goal of user story");
        mockUserStory.setPriority(Priority.MUST_HAVE);
        mockUserStory.setRole("role of user story");
        mockUserStory.setAcceptanceCriteria("acceptance criteria of user story");
        mockUserStory.setAction("action of user story");
        mockUserStory.setEpic(mockEpic);

    }

    @Test
    void testSaveProductBacklog() {
        when(productBacklogRepository.save(mockBacklog)).thenReturn(mockBacklog);

        ProductBacklog saved = productBacklogService.saveProductBacklog(mockBacklog);

        assertNotNull(saved);
        assertEquals("test backlog", saved.getName());
        verify(productBacklogRepository, times(1)).save(mockBacklog);
    }

    @Test
    void testFindById_Success() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(mockBacklog));

        Optional<ProductBacklog> found = productBacklogService.findById(1L);

        assertTrue(found.isPresent());
        assertEquals("test backlog", found.get().getName());
    }

    @Test
    void testFindById_NotFound() {
        when(productBacklogRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<ProductBacklog> found = productBacklogService.findById(99L);

        assertFalse(found.isPresent());
    }

    @Test
    void testUpdateProductBacklog_Success() {
        ProductBacklog updatedBacklog = new ProductBacklog();

        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(mockBacklog));
        when(productBacklogRepository.save(any(ProductBacklog.class))).thenReturn(updatedBacklog);

        ProductBacklog result = productBacklogService.updateProductBacklog(updatedBacklog, 1L);

        assertEquals("Updated Backlog", result.getName());
        verify(productBacklogRepository, times(1)).save(updatedBacklog);
    }

    @Test
    void testAddEpicToProductBacklog_Success() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(mockBacklog));
        when(epicRepository.findById(1L)).thenReturn(Optional.of(mockEpic));
        when(productBacklogRepository.save(any(ProductBacklog.class))).thenReturn(mockBacklog);

        ProductBacklog result = productBacklogService.addEpicToProductBacklog(1L, 1L);

        assertTrue(result.getEpics().contains(mockEpic));
        verify(productBacklogRepository, times(1)).save(mockBacklog);
    }

    @Test
    void testAddEpicToProductBacklog_NotFound() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productBacklogService.addEpicToProductBacklog(1L, 1L));
    }

    @Test
    void testRemoveEpicFromProductBacklog_Success() {
        mockBacklog.getEpics().add(mockEpic);

        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(mockBacklog));
        when(epicRepository.findById(1L)).thenReturn(Optional.of(mockEpic));
        when(productBacklogRepository.save(any(ProductBacklog.class))).thenReturn(mockBacklog);

        ProductBacklog result = productBacklogService.removeEpicFromProductBacklog(1L, 1L);

        assertFalse(result.getEpics().contains(mockEpic));
        verify(productBacklogRepository, times(1)).save(mockBacklog);
    }

    @Test
    void testPrioritizeUserStoriesMoscow() {
        UserStory us1 = new UserStory();
        us1.setId(1L);
        us1.setPriority(Priority.MUST_HAVE);
        UserStory us2 = new UserStory();
        us2.setId(2L);
        us2.setPriority(Priority.SHOULD_HAVE);
        UserStory us3 = new UserStory();
        us3.setId(3L);
        us3.setPriority(Priority.COULD_HAVE);

        mockBacklog.setUserStories(Arrays.asList(us1, us2, us3));

        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(mockBacklog));

        List<UserStory> sortedStories = productBacklogService.prioritizeUserStoriesMoscow(1L);

        assertNotNull(sortedStories);
        assertEquals(3, sortedStories.size());

        // Vérification de l'ordre Must Have (1) -> Should Have (2) -> Could Have (3)
        assertEquals(3L, sortedStories.get(0).getId()); // Story 2 (Must Have)
        assertEquals(2L, sortedStories.get(1).getId()); // Story 1 (Should Have)
        assertEquals(1L, sortedStories.get(2).getId()); // Story 3 (Could Have)
    }

    @Test
    void testPrioritizeUserStoriesMoscow_BacklogNotFound() {
        when(productBacklogRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> productBacklogService.prioritizeUserStoriesMoscow(99L));

        assertEquals("ProductBacklog not found with id: 99", exception.getMessage());
    }
}

