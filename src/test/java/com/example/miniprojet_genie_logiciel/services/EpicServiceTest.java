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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EpicServiceTest {

    @Mock
    private EpicRepository epicRepository;

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @Mock
    private UserStoryRepository userStoryRepository;

    @InjectMocks
    private EpicService epicService;

    private Epic epic;
    private ProductBacklog productBacklog;
    private UserStory userStory;

    @BeforeEach
    void setUp() {
        epic = new Epic();
        epic.setId(1L);
        epic.setTitle("Test Epic");
        epic.setDescription("Test Description");
        epic.setStatus(Status.TO_DO);
        epic.setUserStories(new ArrayList<>());

        productBacklog = new ProductBacklog();
        productBacklog.setId(1L);
        productBacklog.setEpics(new ArrayList<>());

        userStory = new UserStory();
        userStory.setId(1L);
        userStory.setTitle("Test User Story");
    }

    @Test
    void testCreateEpic() {
        when(epicRepository.save(any(Epic.class))).thenReturn(epic);

        Epic created = epicService.createEpic(epic);

        assertNotNull(created);
        assertEquals("Test Epic", created.getTitle());
        verify(epicRepository, times(1)).save(any(Epic.class));
    }

    @Test
    void testGetEpicById() {
        when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));

        Optional<Epic> found = epicService.getEpicById(1L);

        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
        assertEquals("Test Epic", found.get().getTitle());
    }

    @Test
    void testGetEpicById_NotFound() {
        when(epicRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Epic> result = epicService.getEpicById(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllEpics() {
        when(epicRepository.findAll()).thenReturn(List.of(epic));

        List<Epic> epics = epicService.getAllEpics();

        assertFalse(epics.isEmpty());
        assertEquals(1, epics.size());
        assertEquals("Test Epic", epics.get(0).getTitle());
    }

    @Test
    void testUpdateEpic() {
        Epic updatedEpic = new Epic();
        updatedEpic.setTitle("Updated Epic");
        updatedEpic.setDescription("Updated Description");
        updatedEpic.setStatus(Status.IN_PROGRESS);

        when(epicRepository.save(any(Epic.class))).thenReturn(updatedEpic);

        Epic result = epicService.updateEpic(updatedEpic);

        assertNotNull(result);
        assertEquals("Updated Epic", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(Status.IN_PROGRESS, result.getStatus());
    }



    @Test
    void testDeleteEpic() {
        doNothing().when(epicRepository).deleteById(1L);

        epicService.deleteEpic(1L);

        verify(epicRepository, times(1)).deleteById(1L);
    }

    @Test
    void testAddUserStoryToEpic() {
        when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));
        when(epicRepository.save(any(Epic.class))).thenReturn(epic);

        Epic result = epicService.addUserStoryToEpic(1L, userStory);

        assertNotNull(result);
        assertTrue(result.getUserStories().contains(userStory));
        assertEquals(epic, userStory.getEpic());
    }

    @Test
    void testAddUserStoryToEpic_EpicNotFound() {
        when(epicRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> epicService.addUserStoryToEpic(1L, userStory));
    }

    @Test
    void testGetUserStoriesByEpic() {
        List<UserStory> userStories = List.of(userStory);
        epic.setUserStories(userStories);

        when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));

        List<UserStory> result = epicService.getUserStoriesByEpic(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userStory, result.get(0));
    }

    @Test
    void testGetUserStoriesByEpic_EpicNotFound() {
        when(epicRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> epicService.getUserStoriesByEpic(1L));
    }
}