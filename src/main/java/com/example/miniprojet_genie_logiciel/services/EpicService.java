package com.example.miniprojet_genie_logiciel.services;

import com.example.miniprojet_genie_logiciel.entities.Epic;
import com.example.miniprojet_genie_logiciel.entities.UserStory;
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

    // Création d'un Epic
    public Epic createEpic(Epic epic) {
        return epicRepository.save(epic);
    }

    // Récupération de tous les Epics
    public List<Epic> getAllEpics() {
        return epicRepository.findAll();
    }

    // Récupération d'un Epic par son id
    public Optional<Epic> getEpicById(Long id) {
        return epicRepository.findById(id);
    }

    // Mise à jour d'un Epic
    public Epic updateEpic(Epic epic) {
        return epicRepository.save(epic);
    }

    // Suppression d'un Epic
    public void deleteEpic(Long id) {
        epicRepository.deleteById(id);
    }

    // Lien d'une User Story à un Epic
    public Epic addUserStoryToEpic(Long epicId, UserStory userStory) {
        Epic epic = epicRepository.findById(epicId)
                .orElseThrow(() -> new EntityNotFoundException("Epic not found with id: " + epicId));
        if (epic.getUserStories() == null) {
            epic.setUserStories(new ArrayList<>());
        }
        epic.getUserStories().add(userStory);
        // Optionnel : lier l'Epic à la User Story
        userStory.setEpic(epic);
        return epicRepository.save(epic);
    }

    // Visualisation des User Stories liées à un Epic
    public List<UserStory> getUserStoriesByEpic(Long epicId) {
        Epic epic = epicRepository.findById(epicId)
                .orElseThrow(() -> new EntityNotFoundException("Epic not found with id: " + epicId));
        return epic.getUserStories();
    }
}
