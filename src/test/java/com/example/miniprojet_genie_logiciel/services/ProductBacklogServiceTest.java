package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.dto.*;
import com.example.miniprojet_genie_logiciel.entities.*;
import com.example.miniprojet_genie_logiciel.mapper.EpicMapper;
import com.example.miniprojet_genie_logiciel.mapper.ProductBacklogMapper;
import com.example.miniprojet_genie_logiciel.mapper.UserStoryMapper;
import com.example.miniprojet_genie_logiciel.repository.EpicRepository;
import com.example.miniprojet_genie_logiciel.repository.ProductBacklogRepository;
import com.example.miniprojet_genie_logiciel.repository.UserStoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductBacklogServiceTest {

    @Mock
    private ProductBacklogRepository backlogRepository;
    @Mock
    private EpicRepository epicRepository;
    @Mock
    private UserStoryRepository userStoryRepository;
    @Mock
    private ProductBacklogMapper backlogMapper;
    @Mock
    private EpicMapper epicMapper;
    @Mock
    private UserStoryMapper userStoryMapper;

    @InjectMocks
    private ProductBacklogService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // === Test créer un ProductBacklog ===
    @Test
    void shouldCreateBacklog() {
        ProductBacklogDTO dto = new ProductBacklogDTO();
        dto.setName("PB1");

        ProductBacklog entity = new ProductBacklog();
        entity.setId(1L);
        entity.setName("PB1");

        when(backlogMapper.toEntity(dto)).thenReturn(entity);
        when(backlogRepository.save(entity)).thenReturn(entity);
        when(backlogMapper.toDto(entity)).thenReturn(dto);

        ProductBacklogDTO result = service.createBacklog(dto);

        assertEquals("PB1", result.getName());
        verify(backlogRepository).save(entity);
    }

    // === Test findAll ===
    @Test
    void shouldReturnAllBacklogs() {
        List<ProductBacklog> entities = List.of(new ProductBacklog());
        when(backlogRepository.findAll()).thenReturn(entities);
        when(backlogMapper.toDto(any())).thenReturn(new ProductBacklogDTO());

        List<ProductBacklogDTO> result = service.findAll();
        assertEquals(1, result.size());
    }

    // === Test suppression backlog ===
    @Test
    void shouldDeleteBacklog() {
        Long id = 5L;
        service.deleteById(id);
        verify(backlogRepository).deleteById(id);
    }

    // === Test findById - Not Found ===
    @Test
    void shouldThrowWhenBacklogNotFound() {
        Long id = 42L;
        when(backlogRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.updateBacklog(id, new ProductBacklogDTO()));
    }

    @Test
    void prioritizeUserStories_shouldReturnSortedByPriorityWeightDesc() {
        // Arrange
        ProductBacklog backlog = new ProductBacklog();

        UserStory us1 = new UserStory();
        us1.setId(1L);
        us1.setTitle("Story 1");
        us1.setPriority(Priority.COULD_HAVE);

        UserStory us2 = new UserStory();
        us2.setId(2L);
        us2.setTitle("Story 2");
        us2.setPriority(Priority.MUST_HAVE);

        backlog.setUserStories(List.of(us1, us2));

        UserStoryDTO dto1 = new UserStoryDTO();
        dto1.setId(1L);
        dto1.setTitle("Story 1");
        dto1.setPriority(us1.getPriority());

        UserStoryDTO dto2 = new UserStoryDTO();
        dto2.setId(2L);
        dto2.setTitle("Story 2");
        dto2.setPriority(us2.getPriority());

        Mockito.when(backlogRepository.findById(1L)).thenReturn(Optional.of(backlog));
        Mockito.when(userStoryMapper.toDto(us1)).thenReturn(dto1);
        Mockito.when(userStoryMapper.toDto(us2)).thenReturn(dto2);

        // Act
        List<UserStoryDTO> result = service.prioritizeUserStories(1L);

        // Assert
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(2L, result.get(0).getId()); // Story 2 has higher priority
        Assertions.assertEquals(1L, result.get(1).getId()); // Story 1 has lower priority
    }

}


