package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.dto.EpicDTO;
import com.example.miniprojet_genie_logiciel.dto.UserStoryDTO;
import com.example.miniprojet_genie_logiciel.entities.*;
import com.example.miniprojet_genie_logiciel.mapper.EpicMapper;
import com.example.miniprojet_genie_logiciel.mapper.UserStoryMapper;
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

    @Mock
    private EpicMapper epicMapper;

    @Mock
    private UserStoryMapper userStoryMapper;

    @InjectMocks
    private EpicService epicService;

    private Epic epic;
    private EpicDTO epicDTO;
    private ProductBacklog productBacklog;
    private UserStory userStory;
    private UserStoryDTO userStoryDTO;

    @BeforeEach
    void setUp() {
        epic = new Epic();
        epic.setId(1L);
        epic.setTitle("Test Epic");
        epic.setDescription("Test Description");
        epic.setStatus(Status.TO_DO);
        epic.setUserStories(new ArrayList<>());

        epicDTO = new EpicDTO();
        epicDTO.setId(1L);
        epicDTO.setTitle("Test Epic");
        epicDTO.setDescription("Test Description");
        epicDTO.setStatus(Status.TO_DO.toString());
        epicDTO.setUserStories(new ArrayList<>());

        productBacklog = new ProductBacklog();
        productBacklog.setId(1L);
        productBacklog.setEpics(new ArrayList<>());

        userStory = new UserStory();
        userStory.setId(1L);
        userStory.setTitle("Test User Story");

        userStoryDTO = new UserStoryDTO();
        userStoryDTO.setId(1L);
        userStoryDTO.setTitle("Test User Story");
    }

    @Test
    void testCreateEpic() {
        when(epicMapper.toEntity(any(EpicDTO.class))).thenReturn(epic);
        when(epicRepository.save(any(Epic.class))).thenReturn(epic);
        when(epicMapper.toDto(any(Epic.class))).thenReturn(epicDTO);

        EpicDTO created = epicService.createEpic(epicDTO);

        assertNotNull(created);
        assertEquals("Test Epic", created.getTitle());
        verify(epicRepository, times(1)).save(any(Epic.class));
        verify(epicMapper, times(1)).toEntity(any(EpicDTO.class));
        verify(epicMapper, times(1)).toDto(any(Epic.class));
    }

    @Test
    void testGetEpicById() {
        when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));
        when(epicMapper.toDto(any(Epic.class))).thenReturn(epicDTO);

        Optional<EpicDTO> found = epicService.getEpicById(1L);

        assertTrue(found.isPresent());
        assertEquals(1L, found.get().getId());
        assertEquals("Test Epic", found.get().getTitle());
        verify(epicMapper, times(1)).toDto(any(Epic.class));
    }

    @Test
    void testGetEpicById_NotFound() {
        when(epicRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<EpicDTO> result = epicService.getEpicById(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllEpics() {
        when(epicRepository.findAll()).thenReturn(List.of(epic));
        when(epicMapper.toDto(any(Epic.class))).thenReturn(epicDTO);

        List<EpicDTO> epics = epicService.getAllEpics();

        assertFalse(epics.isEmpty());
        assertEquals(1, epics.size());
        assertEquals("Test Epic", epics.get(0).getTitle());
        verify(epicMapper, times(1)).toDto(any(Epic.class));
    }

    @Test
    void testUpdateEpic() {
        EpicDTO updatedEpicDTO = new EpicDTO();
        updatedEpicDTO.setTitle("Updated Epic");
        updatedEpicDTO.setDescription("Updated Description");
        updatedEpicDTO.setStatus(Status.IN_PROGRESS.toString());

        Epic updatedEpic = new Epic();
        updatedEpic.setTitle("Updated Epic");
        updatedEpic.setDescription("Updated Description");
        updatedEpic.setStatus(Status.IN_PROGRESS);

        when(epicMapper.toEntity(any(EpicDTO.class))).thenReturn(updatedEpic);
        when(epicRepository.save(any(Epic.class))).thenReturn(updatedEpic);
        when(epicMapper.toDto(any(Epic.class))).thenReturn(updatedEpicDTO);

        EpicDTO result = epicService.updateEpic(updatedEpicDTO);

        assertNotNull(result);
        assertEquals("Updated Epic", result.getTitle());
        assertEquals("Updated Description", result.getDescription());
        assertEquals(Status.IN_PROGRESS.toString(), result.getStatus());
        verify(epicMapper, times(1)).toEntity(any(EpicDTO.class));
        verify(epicMapper, times(1)).toDto(any(Epic.class));
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
        when(epicMapper.toDto(any(Epic.class))).thenReturn(epicDTO);

        EpicDTO result = epicService.addUserStoryToEpic(1L, userStoryDTO);

        assertNotNull(result);
        verify(epicRepository).save(any(Epic.class));
        verify(epicMapper).toDto(any(Epic.class));
    }

    @Test
    void testAddUserStoryToEpic_EpicNotFound() {
        when(epicRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> epicService.addUserStoryToEpic(1L, userStoryDTO));
    }

    @Test
    void testGetUserStoriesByEpic() {
        List<UserStory> userStories = List.of(userStory);
        epic.setUserStories(userStories);

        when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));
        when(userStoryMapper.toDto(any(UserStory.class))).thenReturn(userStoryDTO);

        List<UserStoryDTO> result = epicService.getUserStoriesByEpic(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userStoryDTO.getId(), result.get(0).getId());
        assertEquals(userStoryDTO.getTitle(), result.get(0).getTitle());
        verify(userStoryMapper, times(1)).toDto(any(UserStory.class));
    }

    @Test
    void testGetUserStoriesByEpic_EpicNotFound() {
        when(epicRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> epicService.getUserStoriesByEpic(1L));
    }
}