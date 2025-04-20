package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.dto.CreateEpicDTO;
import com.example.miniprojet_genie_logiciel.dto.EpicDTO;
import com.example.miniprojet_genie_logiciel.dto.UpdateEpicDTO;
import com.example.miniprojet_genie_logiciel.entities.Epic;
import com.example.miniprojet_genie_logiciel.entities.ProductBacklog;
import com.example.miniprojet_genie_logiciel.mapper.EpicMapper;
import com.example.miniprojet_genie_logiciel.repository.EpicRepository;
import com.example.miniprojet_genie_logiciel.repository.ProductBacklogRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EpicService {

    private final EpicRepository epicRepository;
    private final ProductBacklogRepository productBacklogRepository;
    private final EpicMapper epicMapper;

    public EpicDTO createEpic(CreateEpicDTO dto) {
        ProductBacklog backlog = productBacklogRepository.findById(dto.getProductBacklogId())
                .orElseThrow(() -> new EntityNotFoundException("ProductBacklog not found with id: " + dto.getProductBacklogId()));

        Epic epic = epicMapper.fromCreateDto(dto);
        epic.setProductBacklog(backlog);

        Epic savedEpic = epicRepository.save(epic);
        return epicMapper.toDto(savedEpic);
    }

    public List<EpicDTO> getAllEpics() {
        return epicRepository.findAll().stream()
                .map(epicMapper::toDto)
                .collect(Collectors.toList());
    }

    public EpicDTO getEpicById(Long id) {
        Epic epic = epicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Epic not found with id: " + id));
        return epicMapper.toDto(epic);
    }

    public EpicDTO updateEpic(Long id, UpdateEpicDTO dto) {
        Epic epic = epicRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Epic not found with id: " + id));

        epicMapper.updateFromDto(dto, epic);
        Epic updatedEpic = epicRepository.save(epic);
        return epicMapper.toDto(updatedEpic);
    }

    public void deleteEpic(Long id) {
        if (!epicRepository.existsById(id)) {
            throw new EntityNotFoundException("Epic not found with id: " + id);
        }
        epicRepository.deleteById(id);
    }

    public List<EpicDTO> getEpicsByBacklogId(Long backlogId) {
        ProductBacklog backlog = productBacklogRepository.findById(backlogId)
                .orElseThrow(() -> new EntityNotFoundException("ProductBacklog not found with id: " + backlogId));

        return backlog.getEpics().stream()
                .map(epicMapper::toDto)
                .collect(Collectors.toList());
    }
}
