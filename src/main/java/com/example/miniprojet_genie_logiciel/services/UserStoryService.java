package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.entities.Epic;
import com.example.miniprojet_genie_logiciel.entities.ProductBacklog;
import com.example.miniprojet_genie_logiciel.entities.UserStory;
import com.example.miniprojet_genie_logiciel.repository.EpicRepository;
import com.example.miniprojet_genie_logiciel.repository.ProductBacklogRepository;
import com.example.miniprojet_genie_logiciel.repository.UserStoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserStoryService {

    private final UserStoryRepository userStoryRepository;
    private final EpicRepository epicRepository;
    private final ProductBacklogRepository productBacklogRepository;

    // Ajouter une User Story
    public UserStory addUserStory(UserStory userStory) {
        return userStoryRepository.save(userStory);
    }

    // Modifier une User Story
    public UserStory updateUserStory(UserStory userStory, Long id) {
        Optional<UserStory> userStoryOptional = userStoryRepository.findById(id);

        if (userStoryOptional.isPresent()) {
            UserStory existingUserStory = userStoryOptional.get();
            existingUserStory.setPriority(userStory.getPriority());
            existingUserStory.setStatus(userStory.getStatus());
            existingUserStory.setValue(userStory.getValue());
            existingUserStory.setRole(userStory.getRole());
            existingUserStory.setAction(userStory.getAction());
            existingUserStory.setTitle(userStory.getTitle());
            return userStoryRepository.save(existingUserStory);
        }
        else return null;
    }

    // Supprimer une User Story
    public void deleteUserStory(Long id) {
        if (!userStoryRepository.existsById(id)) {
            throw new EntityNotFoundException("User Story not found with id: " + id);
        }
        userStoryRepository.deleteById(id);
    }

    // Lier une User Story à un Epic
    public UserStory linkUserStoryToEpic(Long userStoryId, Long epicId) {
        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new EntityNotFoundException("User Story not found with id: " + userStoryId));

        Epic epic = epicRepository.findById(epicId)
                .orElseThrow(() -> new EntityNotFoundException("Epic not found with id: " + epicId));

        userStory.setEpic(epic);
        return userStoryRepository.save(userStory);
    }

    // Définir les critères d'acceptation (ajout de critères sous forme de texte dans la valeur)
    public UserStory setAcceptanceCriteria(Long userStoryId, String criteria) {
        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new EntityNotFoundException("User Story not found with id: " + userStoryId));

        userStory.setValue(criteria);
        return userStoryRepository.save(userStory);
    }

    // Mettre à jour le statut d'une User Story (To Do, In Progress, Done)
    public UserStory updateUserStoryStatus(Long userStoryId, String status) {
        UserStory userStory = userStoryRepository.findById(userStoryId)
                .orElseThrow(() -> new EntityNotFoundException("User Story not found with id: " + userStoryId));

        userStory.setStatus(status);
        return userStoryRepository.save(userStory);
    }

    // Prioriser les User Stories dans le Product Backlog en utilisant MoSCoW
    public List<UserStory> prioritizeUserStories(Long backlogId) {
        ProductBacklog pb = productBacklogRepository.findById(backlogId)
                .orElseThrow(() -> new EntityNotFoundException("Product Backlog not found with id: " + backlogId));

        List<UserStory> stories = pb.getUserStories();

        // Tri basé sur la priorité (MoSCoW : Must Have > Should Have > Could Have > Won't Have)
        return stories.stream()
                .sorted((us1, us2) -> {
                    int priority1 = getPriorityValue(us1.getPriority());
                    int priority2 = getPriorityValue(us2.getPriority());
                    return Integer.compare(priority2, priority1); // Tri décroissant
                })
                .collect(Collectors.toList());
    }

    // Méthode d'aide pour attribuer une valeur aux priorités MoSCoW
    private int getPriorityValue(String priority) {
        return switch (priority) {
            case "Must Have" -> 4;
            case "Should Have" -> 3;
            case "Could Have" -> 2;
            case "Won't Have" -> 1;
            default -> 0;
        };
    }
}
