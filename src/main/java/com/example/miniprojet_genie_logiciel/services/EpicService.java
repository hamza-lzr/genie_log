package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.dto.EpicDTO;
import com.example.miniprojet_genie_logiciel.dto.UserStoryDTO;
import com.example.miniprojet_genie_logiciel.entities.Epic;
import com.example.miniprojet_genie_logiciel.entities.UserStory;
import com.example.miniprojet_genie_logiciel.mapper.EpicMapper;
import com.example.miniprojet_genie_logiciel.mapper.UserStoryMapper;
import com.example.miniprojet_genie_logiciel.repository.EpicRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EpicService {

    private final EpicRepository epicRepository;
    private final EpicMapper epicMapper;
    private final UserStoryMapper userStoryMapper;

    // Création d'un Epic
    public EpicDTO createEpic(EpicDTO epicDTO) {
        Epic epic = epicMapper.toEntity(epicDTO);
        Epic savedEpic = epicRepository.save(epic);
        return epicMapper.toDto(savedEpic);
    }

    // Récupération de tous les Epics
    public List<EpicDTO> getAllEpics() {
        List<Epic> epics = epicRepository.findAll();
        return epics.stream()
                .map(epicMapper::toDto)
                .toList();
    }

    // Récupération d'un Epic par son id
    public Optional<EpicDTO> getEpicById(Long id) {
        return epicRepository.findById(id)
                .map(epicMapper::toDto);
    }

    // Mise à jour d'un Epic
    public EpicDTO updateEpic(EpicDTO epicDTO) {
        Epic epic = epicMapper.toEntity(epicDTO);
        Epic updatedEpic = epicRepository.save(epic);
        return epicMapper.toDto(updatedEpic);
    }

    // Suppression d'un Epic
    public void deleteEpic(Long id) {
        epicRepository.deleteById(id);
    }

    // Lien d'une User Story à un Epic
    public EpicDTO addUserStoryToEpic(Long epicId, UserStoryDTO userStoryDTO) {
        Epic epic = epicRepository.findById(epicId)
                .orElseThrow(() -> new EntityNotFoundException("Epic not found with id: " + epicId));
        if (epic.getUserStories() == null) {
            epic.setUserStories(new ArrayList<>());
        }
        UserStory userStory = userStoryMapper.toEntity(userStoryDTO);
        epic.getUserStories().add(userStory);
        userStory.setEpic(epic);
        Epic savedEpic = epicRepository.save(epic);
        return epicMapper.toDto(savedEpic);
    }

    // Visualisation des User Stories liées à un Epic
    public List<UserStoryDTO> getUserStoriesByEpic(Long epicId) {
        Epic epic = epicRepository.findById(epicId)
                .orElseThrow(() -> new EntityNotFoundException("Epic not found with id: " + epicId));
        return epic.getUserStories().stream()
                .map(userStoryMapper::toDto)
                .toList();
    }
}
