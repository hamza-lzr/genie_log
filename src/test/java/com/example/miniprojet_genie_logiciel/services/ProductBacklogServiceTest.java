package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.entities.*;
import com.example.miniprojet_genie_logiciel.repository.EpicRepository;
import com.example.miniprojet_genie_logiciel.repository.ProductBacklogRepository;
import com.example.miniprojet_genie_logiciel.repository.UserStoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductBacklogServiceTest {

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @Mock
    private EpicRepository epicRepository;

    @Mock
    private UserStoryRepository userStoryRepository;

    @InjectMocks
    private ProductBacklogService productBacklogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveProductBacklog() {
        ProductBacklog backlog = new ProductBacklog();
        backlog.setName("Test Backlog");

        when(productBacklogRepository.save(backlog)).thenReturn(backlog);

        ProductBacklog saved = productBacklogService.saveProductBacklog(backlog);
        assertEquals("Test Backlog", saved.getName());
    }

    @Test
    void testFindAll() {
        List<ProductBacklog> list = List.of(new ProductBacklog(), new ProductBacklog());
        when(productBacklogRepository.findAll()).thenReturn(list);

        List<ProductBacklog> result = productBacklogService.findAll();
        assertEquals(2, result.size());
    }

    @Test
    void testAddEpicToProductBacklog() {
        ProductBacklog pb = new ProductBacklog();
        pb.setId(1L);
        pb.setEpics(new ArrayList<>());

        Epic epic = new Epic();
        epic.setId(2L);

        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(pb));
        when(epicRepository.findById(2L)).thenReturn(Optional.of(epic));
        when(productBacklogRepository.save(pb)).thenReturn(pb);

        ProductBacklog result = productBacklogService.addEpicToProductBacklog(1L, 2L);
        assertTrue(result.getEpics().contains(epic));
    }

    @Test
    void testAddUserStoryToProductBacklog() {
        ProductBacklog pb = new ProductBacklog();
        pb.setId(1L);
        pb.setUserStories(new ArrayList<>());

        UserStory us = new UserStory();
        us.setId(2L);

        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(pb));
        when(userStoryRepository.findById(2L)).thenReturn(Optional.of(us));
        when(productBacklogRepository.save(pb)).thenReturn(pb);

        ProductBacklog result = productBacklogService.addUserStoryToProductBacklog(1L, 2L);
        assertTrue(result.getUserStories().contains(us));
        assertEquals(pb, us.getProductBacklog());
    }

    @Test
    void testPrioritizeUserStoriesMoscow() {
        ProductBacklog pb = new ProductBacklog();
        UserStory us1 = new UserStory();
        UserStory us2 = new UserStory();

        Priority high = Priority.MUST_HAVE; // Assume this enum has a weight() method returning int
        Priority low = Priority.COULD_HAVE;

        us1.setPriority(high);
        us2.setPriority(low);

        pb.setUserStories(List.of(us2, us1));

        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(pb));

        List<UserStory> result = productBacklogService.prioritizeUserStoriesMoscow(1L);

        assertEquals(high, result.get(0).getPriority());
    }

    @Test
    void testRemoveEpicFromProductBacklog_NotFound() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                productBacklogService.removeEpicFromProductBacklog(1L, 2L));
        assertTrue(exception.getMessage().contains("ProductBacklog not found"));
    }

}


