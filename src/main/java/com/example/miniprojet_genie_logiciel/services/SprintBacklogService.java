package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.dto.SprintBacklogDTO;
import com.example.miniprojet_genie_logiciel.dto.UserStoryDTO;
import com.example.miniprojet_genie_logiciel.entities.SprintBacklog;
import com.example.miniprojet_genie_logiciel.entities.UserStory;
import com.example.miniprojet_genie_logiciel.mapper.SprintBacklogMapper;
import com.example.miniprojet_genie_logiciel.mapper.UserStoryMapper;
import com.example.miniprojet_genie_logiciel.repository.SprintBacklogRepository;
import com.example.miniprojet_genie_logiciel.repository.UserStoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class SprintBacklogService {

    private final SprintBacklogRepository sprintBacklogRepository;
    private final UserStoryRepository userStoryRepository;
    private final SprintBacklogMapper sprintBacklogMapper;
    private final UserStoryMapper userStoryMapper;

    // Créer un nouveau sprint vide
    public SprintBacklogDTO createSprint(String name) {
        SprintBacklog sprint = new SprintBacklog();
        sprint.setName(name);
        sprint.setUserStories(List.of()); // initialise à liste vide
        SprintBacklog savedSprint = sprintBacklogRepository.save(sprint);
        return sprintBacklogMapper.toDto(savedSprint);
    }

    // Ajouter une User Story à un sprint
    public SprintBacklogDTO addUserStoryToSprint(Long sprintId, Long userStoryId) {
        SprintBacklog sprint = sprintBacklogRepository.findById(sprintId)
                .orElseThrow(() -> new EntityNotFoundException("Sprint non trouvé avec l'id : " + sprintId));
        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new EntityNotFoundException("User Story non trouvée avec l'id : " + userStoryId));

        // Optionnel : empêcher l’ajout si déjà assignée
        if (userStory.getSprintBacklog() != null) {
            throw new IllegalStateException("La User Story est déjà assignée à un sprint.");
        }

        // Lier et sauvegarder
        userStory.setSprintBacklog(sprint);
        sprint.getUserStories().add(userStory);

        SprintBacklog savedSprint = sprintBacklogRepository.save(sprint);
        return sprintBacklogMapper.toDto(savedSprint);
    }

    // Retirer une User Story d'un Sprint
    public void removeUserStoryFromSprint(Long sprintId, Long userStoryId) {
        SprintBacklog sprint = sprintBacklogRepository.findById(sprintId)
                .orElseThrow(() -> new EntityNotFoundException("Sprint non trouvé avec l'id : " + sprintId));
        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new EntityNotFoundException("User Story non trouvée avec l'id : " + userStoryId));

        if (!sprint.getUserStories().contains(userStory)) {
            throw new IllegalArgumentException("La User Story n'est pas présente dans ce sprint.");
        }

        sprint.getUserStories().remove(userStory);
        userStory.setSprintBacklog(null); // désassigner
    }

    // Récupérer tous les sprints
    public List<SprintBacklogDTO> findAllSprints() {
        return sprintBacklogRepository.findAll().stream()
                .map(sprintBacklogMapper::toDto)
                .toList();
    }

    // Récupérer un sprint par ID
    public SprintBacklogDTO findSprintById(Long sprintId) {
        SprintBacklog sprint = sprintBacklogRepository.findById(sprintId)
                .orElseThrow(() -> new EntityNotFoundException("Sprint non trouvé avec l'id : " + sprintId));
        return sprintBacklogMapper.toDto(sprint);
    }

    // Supprimer un sprint par ID
    public void deleteSprintById(Long sprintId) {
        if (!sprintBacklogRepository.existsById(sprintId)) {
            throw new EntityNotFoundException("Sprint non trouvé avec l'id : " + sprintId);
        }
        sprintBacklogRepository.deleteById(sprintId);
    }

}

