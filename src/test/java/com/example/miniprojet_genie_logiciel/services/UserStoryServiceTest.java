package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.dto.CreateUserStoryDTO;
import com.example.miniprojet_genie_logiciel.dto.UpdateUserStoryDTO;
import com.example.miniprojet_genie_logiciel.dto.UserStoryDTO;
import com.example.miniprojet_genie_logiciel.entities.*;
import com.example.miniprojet_genie_logiciel.mapper.UserStoryMapper;
import com.example.miniprojet_genie_logiciel.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserStoryServiceTest {

    @Mock
    private UserStoryRepository userStoryRepository;

    @Mock
    private EpicRepository epicRepository;

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @Mock
    private SprintBacklogRepository sprintBacklogRepository;

    @Mock
    private UserStoryMapper userStoryMapper;

    @InjectMocks
    private UserStoryService userStoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUserStory_shouldReturnDTO() {
        CreateUserStoryDTO dto = new CreateUserStoryDTO();
        dto.setEpicId(1L);
        dto.setProductBacklogId(2L);

        Epic epic = new Epic();
        ProductBacklog backlog = new ProductBacklog();
        UserStory story = new UserStory();
        UserStory saved = new UserStory();
        UserStoryDTO expectedDto = new UserStoryDTO();

        when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));
        when(productBacklogRepository.findById(2L)).thenReturn(Optional.of(backlog));
        when(userStoryMapper.fromCreateDto(dto, epic, backlog)).thenReturn(story);
        when(userStoryRepository.save(story)).thenReturn(saved);
        when(userStoryMapper.toDto(saved)).thenReturn(expectedDto);

        UserStoryDTO result = userStoryService.createUserStory(dto);
        assertEquals(expectedDto, result);
    }

    @Test
    void getAllUserStories_shouldReturnList() {
        when(userStoryRepository.findAll()).thenReturn(List.of(new UserStory(), new UserStory()));
        when(userStoryMapper.toDto(any())).thenReturn(new UserStoryDTO());

        List<UserStoryDTO> result = userStoryService.getAllUserStories();
        assertEquals(2, result.size());
    }

    @Test
    void getUserStoryById_shouldReturnDTO() {
        UserStory story = new UserStory();
        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(story));
        when(userStoryMapper.toDto(story)).thenReturn(new UserStoryDTO());

        UserStoryDTO result = userStoryService.getUserStoryById(1L);
        assertNotNull(result);
    }

    @Test
    void updateUserStory_shouldUpdateAndReturnDto() {
        UpdateUserStoryDTO dto = new UpdateUserStoryDTO();
        dto.setSprintBacklogId(3L);

        UserStory story = new UserStory();
        SprintBacklog sprintBacklog = new SprintBacklog();
        UserStory updated = new UserStory();
        UserStoryDTO expectedDto = new UserStoryDTO();

        when(userStoryRepository.findById(1L)).thenReturn(Optional.of(story));
        when(sprintBacklogRepository.findById(3L)).thenReturn(Optional.of(sprintBacklog));

        doAnswer(invocation -> {
            UserStory us = invocation.getArgument(1);
            us.setTitle("updated"); // simulate mapping
            return null;
        }).when(userStoryMapper).updateFromDto(eq(dto), eq(story), eq(sprintBacklog));

        when(userStoryRepository.save(story)).thenReturn(updated);
        when(userStoryMapper.toDto(updated)).thenReturn(expectedDto);

        UserStoryDTO result = userStoryService.updateUserStory(1L, dto);
        assertEquals(expectedDto, result);
    }

    @Test
    void deleteUserStory_shouldCallDeleteIfExists() {
        when(userStoryRepository.existsById(1L)).thenReturn(true);
        userStoryService.deleteUserStory(1L);
        verify(userStoryRepository).deleteById(1L);
    }

    @Test
    void getUserStoriesByEpic_shouldReturnList() {
        Epic epic = new Epic();
        epic.setUserStories(List.of(new UserStory()));

        when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));
        when(userStoryMapper.toDto(any())).thenReturn(new UserStoryDTO());

        List<UserStoryDTO> result = userStoryService.getUserStoriesByEpic(1L);
        assertEquals(1, result.size());
    }

    @Test
    void getUserStoriesBySprintBacklog_shouldReturnList() {
        SprintBacklog sprint = new SprintBacklog();
        sprint.setUserStories(List.of(new UserStory()));

        when(sprintBacklogRepository.findById(1L)).thenReturn(Optional.of(sprint));
        when(userStoryMapper.toDto(any())).thenReturn(new UserStoryDTO());

        List<UserStoryDTO> result = userStoryService.getUserStoriesBySprintBacklog(1L);
        assertEquals(1, result.size());
    }
}

