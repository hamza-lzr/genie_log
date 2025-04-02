package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.entities.*;
import com.example.miniprojet_genie_logiciel.repository.EpicRepository;
import com.example.miniprojet_genie_logiciel.repository.ProductBacklogRepository;
import com.example.miniprojet_genie_logiciel.repository.UserStoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserStoryServiceTest {

    @Mock
    private UserStoryRepository userStoryRepository;

    @Mock
    private EpicRepository epicRepository;

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @InjectMocks
    private UserStoryService userStoryService;

    private UserStory userStory;
    private ProductBacklog backlog;

    @BeforeEach
    void setUp() {
        userStory = new UserStory();
        userStory.setId(1L);
        userStory.setTitle("User Story Test");
        userStory.setRole("Développeur");
        userStory.setAction("Créer un test");
        userStory.setGoal("Vérifier le bon fonctionnement");
        userStory.setPriority(Priority.MUST_HAVE);
        userStory.setStatus(Status.TO_DO);

        backlog = new ProductBacklog();
        backlog.setId(1L);
        backlog.setUserStories(List.of(userStory));
    }

    // ✅ Test de l'ajout d'une User Story
    @Test
    void testAddUserStory() {
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(userStory);

        UserStory saved = userStoryService.addUserStory(userStory);

        assertNotNull(saved);
        assertEquals("User Story Test", saved.getTitle());
        verify(userStoryRepository, times(1)).save(any(UserStory.class));
    }

    // ✅ Test de mise à jour d'une User Story
    @Test
    void testUpdateUserStory() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));
        when(userStoryRepository.save(any(UserStory.class))).thenReturn(userStory);

        UserStory updatedStory = new UserStory();
        updatedStory.setTitle("Updated Story");
        updatedStory.setRole("PO");
        updatedStory.setAction("Modifier une US");
        updatedStory.setGoal("Mise à jour correcte");
        updatedStory.setPriority(Priority.SHOULD_HAVE);
        updatedStory.setStatus(Status.IN_PROGRESS);

        UserStory result = userStoryService.updateUserStory(1L, updatedStory);

        assertEquals("Updated Story", result.getTitle());
        assertEquals("PO", result.getRole());
        verify(userStoryRepository, times(1)).save(any(UserStory.class));
    }

    // ✅ Test de suppression d'une User Story
    @Test
    void testDeleteUserStory() {
        when(userStoryRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userStoryRepository).deleteById(1L);

        assertDoesNotThrow(() -> userStoryService.deleteUserStory(1L));
        verify(userStoryRepository, times(1)).deleteById(1L);
    }

    // ❌ Test de suppression d'une User Story inexistante
    @Test
    void testDeleteUserStory_NotFound() {
        when(userStoryRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class, () -> userStoryService.deleteUserStory(1L));
        assertEquals("User Story non trouvée avec l'ID : 1", exception.getMessage());
    }

    // ✅ Test de priorisation des User Stories
    @Test
    void testPrioritizeUserStories() {
        UserStory us1 = new UserStory();
        us1.setPriority(Priority.MUST_HAVE);

        UserStory us2 = new UserStory();
        us2.setPriority(Priority.SHOULD_HAVE);

        ProductBacklog backlog = new ProductBacklog();
        backlog.setId(1L);
        backlog.setUserStories(Arrays.asList(us2, us1)); // Mauvais ordre initial

        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(backlog));

        userStoryService.prioritizeUserStories(1L);

        // Vérification que l'ordre est maintenant correct
        assertEquals(Priority.SHOULD_HAVE, backlog.getUserStories().get(1).getPriority());
    }

    // ✅ Test de récupération d'une User Story par ID
    @Test
    void testGetUserStoryById() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(userStory));

        UserStory found = userStoryService.getUserStoryById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
    }

    // ❌ Test de récupération d'une User Story inexistante
    @Test
    void testGetUserStoryById_NotFound() {
        when(userStoryRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> userStoryService.getUserStoryById(1L));
        assertEquals("User Story non trouvée avec l'ID : 1", exception.getMessage());
    }

    // ✅ Test de récupération de toutes les User Stories
    @Test
    void testGetAllUserStories() {
        when(userStoryRepository.findAll()).thenReturn(List.of(userStory));

        List<UserStory> stories = userStoryService.getAllUserStories();

        assertFalse(stories.isEmpty());
        assertEquals(1, stories.size());
    }
}

