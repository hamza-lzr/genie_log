package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.dto.*;
import com.example.miniprojet_genie_logiciel.entities.*;
import com.example.miniprojet_genie_logiciel.mapper.EpicMapper;
import com.example.miniprojet_genie_logiciel.mapper.ProductBacklogMapper;
import com.example.miniprojet_genie_logiciel.mapper.UserStoryMapper;
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

    @Mock
    private ProductBacklogMapper productBacklogMapper;

    @Mock
    private EpicMapper epicMapper;

    @Mock
    private UserStoryMapper userStoryMapper;

    @InjectMocks
    private ProductBacklogService productBacklogService;

    private ProductBacklog productBacklog;
    private ProductBacklogDTO productBacklogDTO;
    private Epic epic;
    private EpicDTO epicDTO;
    private UserStory userStory;
    private UserStoryDTO userStoryDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        productBacklog = new ProductBacklog();
        productBacklog.setId(1L);
        productBacklog.setName("Test Backlog");

        productBacklogDTO = new ProductBacklogDTO();
        productBacklogDTO.setId(1L);
        productBacklogDTO.setName("Test Backlog");

        epic = new Epic();
        epic.setId(2L);
        epic.setTitle("Test Epic");

        epicDTO = new EpicDTO();
        epicDTO.setId(2L);
        epicDTO.setTitle("Test Epic");

        userStory = new UserStory();
        userStory.setId(3L);
        userStory.setTitle("Test User Story");

        userStoryDTO = new UserStoryDTO();
        userStoryDTO.setId(3L);
        userStoryDTO.setTitle("Test User Story");
    }

    @Test
    void testSaveProductBacklog() {
        when(productBacklogMapper.toEntity(any(ProductBacklogDTO.class))).thenReturn(productBacklog);
        when(productBacklogRepository.save(any(ProductBacklog.class))).thenReturn(productBacklog);
        when(productBacklogMapper.toDto(any(ProductBacklog.class))).thenReturn(productBacklogDTO);

        ProductBacklogDTO saved = productBacklogService.saveProductBacklog(productBacklogDTO);
        
        assertEquals("Test Backlog", saved.getName());
        verify(productBacklogMapper).toEntity(any(ProductBacklogDTO.class));
        verify(productBacklogRepository).save(any(ProductBacklog.class));
        verify(productBacklogMapper).toDto(any(ProductBacklog.class));
    }

    @Test
    void testFindAll() {
        List<ProductBacklog> list = List.of(productBacklog);
        when(productBacklogRepository.findAll()).thenReturn(list);
        when(productBacklogMapper.toDto(any(ProductBacklog.class))).thenReturn(productBacklogDTO);

        List<ProductBacklogDTO> result = productBacklogService.findAll();
        
        assertEquals(1, result.size());
        assertEquals("Test Backlog", result.get(0).getName());
        verify(productBacklogMapper).toDto(any(ProductBacklog.class));
    }

    @Test
    void testAddEpicToProductBacklog() {
        productBacklog.setEpics(new ArrayList<>());

        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(productBacklog));
        when(epicRepository.findById(2L)).thenReturn(Optional.of(epic));
        when(productBacklogRepository.save(any(ProductBacklog.class))).thenReturn(productBacklog);
        when(productBacklogMapper.toDto(any(ProductBacklog.class))).thenReturn(productBacklogDTO);

        ProductBacklogDTO result = productBacklogService.addEpicToProductBacklog(1L, 2L);
        
        verify(productBacklogRepository).save(any(ProductBacklog.class));
        verify(productBacklogMapper).toDto(any(ProductBacklog.class));
    }

    @Test
    void testAddUserStoryToProductBacklog() {
        productBacklog.setUserStories(new ArrayList<>());

        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(productBacklog));
        when(userStoryRepository.findById(2L)).thenReturn(Optional.of(userStory));
        when(productBacklogRepository.save(any(ProductBacklog.class))).thenReturn(productBacklog);
        when(productBacklogMapper.toDto(any(ProductBacklog.class))).thenReturn(productBacklogDTO);

        ProductBacklogDTO result = productBacklogService.addUserStoryToProductBacklog(1L, 2L);
        
        verify(productBacklogRepository).save(any(ProductBacklog.class));
        verify(productBacklogMapper).toDto(any(ProductBacklog.class));
    }

    @Test
    void testPrioritizeUserStoriesMoscow() {
        UserStory us1 = new UserStory();
        UserStory us2 = new UserStory();
        us1.setPriority(Priority.MUST_HAVE);
        us2.setPriority(Priority.COULD_HAVE);

        UserStoryDTO usDTO1 = new UserStoryDTO();
        UserStoryDTO usDTO2 = new UserStoryDTO();
        
        PriorityDTO priority1 = new PriorityDTO();
        priority1.setName(Priority.MUST_HAVE.getLabel());
        priority1.setWeight(Priority.MUST_HAVE.getWeight());
        
        PriorityDTO priority2 = new PriorityDTO();
        priority2.setName(Priority.COULD_HAVE.getLabel());
        priority2.setWeight(Priority.COULD_HAVE.getWeight());
        
        usDTO1.setPriority(priority1);
        usDTO2.setPriority(priority2);

        productBacklog.setUserStories(List.of(us2, us1));

        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(productBacklog));
        when(userStoryMapper.toDto(us1)).thenReturn(usDTO1);
        when(userStoryMapper.toDto(us2)).thenReturn(usDTO2);

        List<UserStoryDTO> result = productBacklogService.prioritizeUserStoriesMoscow(1L);

        assertEquals("Must Have", result.get(0).getPriority().getName());
        assertEquals(4, result.get(0).getPriority().getWeight());
        verify(userStoryMapper, times(2)).toDto(any(UserStory.class));
    }

    @Test
    void testRemoveEpicFromProductBacklog_NotFound() {
        when(productBacklogRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                productBacklogService.removeEpicFromProductBacklog(1L, 2L));
        assertTrue(exception.getMessage().contains("ProductBacklog not found"));
    }

}


