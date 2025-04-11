package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.entities.SprintBacklog;
import com.example.miniprojet_genie_logiciel.entities.UserStory;
import com.example.miniprojet_genie_logiciel.entities.Task;
import com.example.miniprojet_genie_logiciel.repository.SprintBacklogRepository;
import com.example.miniprojet_genie_logiciel.repository.UserStoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class SprintBacklogService {

    private final SprintBacklogRepository sprintBacklogRepository;
    private final UserStoryRepository userStoryRepository;

    // Créer un nouveau sprint vide
    public SprintBacklog createSprint(String name) {
        SprintBacklog sprint = new SprintBacklog();
        sprint.setName(name);
        sprint.setUserStories(List.of()); // initialise à liste vide
        return sprintBacklogRepository.save(sprint);
    }

    // Ajouter une User Story à un sprint
    public String addUserStoryToSprint(Long sprintId, Long userStoryId) {
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

        return "User Story [id=" + userStory.getId() + "] ajoutée au Sprint '" + sprint.getName() + "' [id=" + sprint.getId() + "]";
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
    public List<SprintBacklog> findAllSprints() {
        return sprintBacklogRepository.findAll();
    }

    // Récupérer un sprint par ID
    public SprintBacklog findSprintById(Long sprintId) {
        return sprintBacklogRepository.findById(sprintId)
                .orElseThrow(() -> new EntityNotFoundException("Sprint non trouvé avec l'id : " + sprintId));
    }

    // Supprimer un sprint par ID
    public void deleteSprintById(Long sprintId) {
        if (!sprintBacklogRepository.existsById(sprintId)) {
            throw new EntityNotFoundException("Sprint non trouvé avec l'id : " + sprintId);
        }
        sprintBacklogRepository.deleteById(sprintId);
    }

}

