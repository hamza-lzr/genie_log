package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.dto.CreateEpicDTO;
import com.example.miniprojet_genie_logiciel.dto.EpicDTO;
import com.example.miniprojet_genie_logiciel.dto.UpdateEpicDTO;
import com.example.miniprojet_genie_logiciel.entities.Epic;
import com.example.miniprojet_genie_logiciel.entities.ProductBacklog;
import com.example.miniprojet_genie_logiciel.entities.Status;
import com.example.miniprojet_genie_logiciel.mapper.EpicMapper;
import com.example.miniprojet_genie_logiciel.repository.EpicRepository;
import com.example.miniprojet_genie_logiciel.repository.ProductBacklogRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EpicServiceTest {

    @Mock
    private EpicRepository epicRepository;

    @Mock
    private ProductBacklogRepository productBacklogRepository;

    @Mock
    private EpicMapper epicMapper;

    @InjectMocks
    private EpicService epicService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createEpic_shouldReturnEpicDTO() {
        CreateEpicDTO dto = new CreateEpicDTO();
        dto.setTitle("Title");
        dto.setDescription("Description");
        dto.setStatus(Status.DONE);
        dto.setProductBacklogId(1L);
        ProductBacklog backlog = new ProductBacklog();
        Epic epic = new Epic();
        Epic savedEpic = new Epic();
        EpicDTO expectedDto = new EpicDTO();

        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(backlog));
        when(epicMapper.fromCreateDto(dto)).thenReturn(epic);
        when(epicRepository.save(epic)).thenReturn(savedEpic);
        when(epicMapper.toDto(savedEpic)).thenReturn(expectedDto);

        EpicDTO result = epicService.createEpic(dto);

        assertEquals(expectedDto, result);
    }

    @Test
    void getAllEpics_shouldReturnList() {
        List<Epic> epics = List.of(new Epic(), new Epic());
        when(epicRepository.findAll()).thenReturn(epics);
        when(epicMapper.toDto(any())).thenReturn(new EpicDTO());

        List<EpicDTO> result = epicService.getAllEpics();

        assertEquals(2, result.size());
    }

    @Test
    void getEpicById_shouldReturnDto() {
        Epic epic = new Epic();
        when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));
        when(epicMapper.toDto(epic)).thenReturn(new EpicDTO());

        EpicDTO result = epicService.getEpicById(1L);

        assertNotNull(result);
    }

    @Test
    void updateEpic_shouldReturnUpdatedDto() {
        Epic epic = new Epic();
        UpdateEpicDTO dto = new UpdateEpicDTO();
        dto.setTitle("Title");
        dto.setDescription("Description");
        dto.setStatus(Status.TO_DO);

        when(epicRepository.findById(1L)).thenReturn(Optional.of(epic));
        when(epicRepository.save(epic)).thenReturn(epic);
        when(epicMapper.toDto(epic)).thenReturn(new EpicDTO());

        EpicDTO result = epicService.updateEpic(1L, dto);

        assertNotNull(result);
    }

    @Test
    void deleteEpic_shouldCallRepository() {
        when(epicRepository.existsById(1L)).thenReturn(true);

        epicService.deleteEpic(1L);

        verify(epicRepository).deleteById(1L);
    }

    @Test
    void getEpicsByBacklogId_shouldReturnList() {
        Epic epic = new Epic();
        ProductBacklog backlog = new ProductBacklog();
        backlog.setEpics(List.of(epic));

        when(productBacklogRepository.findById(1L)).thenReturn(Optional.of(backlog));
        when(epicMapper.toDto(epic)).thenReturn(new EpicDTO());

        List<EpicDTO> result = epicService.getEpicsByBacklogId(1L);

        assertEquals(1, result.size());
    }
}

